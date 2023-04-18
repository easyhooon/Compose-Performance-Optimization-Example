package kr.co.fastcampus.part4plus.chapter2.ui.content

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kr.co.fastcampus.part4plus.chapter2.R
import kr.co.fastcampus.part4plus.chapter2.model.memos

private val MinTitleOffset = 20.dp
private val MaxTitleOffset = 100.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun ContentScreen(memoId: Int) {
    val memo = remember(memos) { memos.single { it.id == memoId } }

    Box(Modifier.fillMaxSize()) {
        // 화면에서 사용하는 Scroll 값을 State 로 처리한 것
        // rememberScrollState 를 사용하면 여러가지 컴포넌트에서 Scroll 에 관련된 값들을 사용할 수 있음
        val scroll = rememberScrollState(0)
        Body(scroll)
        //TODO 어떻게 Title 함수를 수정해야하는지 잘 모르겠다. 학습 필값
        Title(memo.text) {scroll.value}
    }
}

@Composable
private fun Body(
    scroll: ScrollState
) {
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaxTitleOffset)
        )
        Column(
            modifier = Modifier
                .verticalScroll(scroll)
        ) {
            Surface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(110.dp))
                    Text(
                        text = stringResource(R.string.detail_placeholder),
                        style = MaterialTheme.typography.body1,
                        color = Color.Black,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun Title(memoText: String, scrollProvider: () -> Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    Column(
        modifier = Modifier
            .heightIn(min = MaxTitleOffset)
            .offset {
                // scroll 값을 통해 offset 을 계산
                // 위아래 스크롤를 왔다갔다 하며넛 Column 부분을 계속해서 그려줌
                // Recomposition 이 계속해서 발생하는 이유
                // Title 이 호출될때마다 계속해서 offset 이 변경됨, 전체 Modifier 가 변경됨
                // 결국 Column 전체가 Recomposition 됨 (Ui 트리를 매번 새로 그림)
                // -> scroll 파라미터를 람다로 바꿔야 한다!
                // 람다를 참조하는 형태로 변경되어 offset의 값은 변하지 않게 됨
                // int 값을 받는게 아니라 람다를 참조하는 형태라 offset이 참조하는 것은 똑같음
                // 값을 직접 전달하지 않고, 람다를 참조하는 형태로 변경하여 Composition 단계를 생략 해줄 수 있었음
                // ㄴ 상태 읽기 연기 전략
                val offset = (maxOffset - scrollProvider()).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Text(
            text = memoText,
            style = MaterialTheme.typography.h4,
            color = Color.Black,
            modifier = HzPadding
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "MEMO",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primaryVariant,
            modifier = HzPadding
        )

        Spacer(Modifier.height(8.dp))
    }
}
