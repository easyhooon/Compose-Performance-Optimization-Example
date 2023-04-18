package kr.co.fastcampus.part4plus.chapter2.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import kr.co.fastcampus.part4plus.chapter2.model.Memo
import kr.co.fastcampus.part4plus.chapter2.model.memos
import kr.co.fastcampus.part4plus.chapter2.ui.theme.MemoAppTheme

@Composable
fun HomeScreen(homeState: HomeState) {
    MemoAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val memoList = remember { memos }
            val onClickAction: (Int) -> Unit = {
                homeState.showContent(
                    it
                )
            }

            Column {
                AddMemo(memoList)
                MemoList(onClickAction, memoList)
            }
        }
    }
}

@Composable
fun AddMemo(memoList: SnapshotStateList<Memo>) {
    val inputValue = remember { mutableStateOf("") }
    var count by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .padding(all = 16.dp)
            .height(100.dp),
        horizontalArrangement = Arrangement.End
    ) {
        TextField(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            value = inputValue.value,
            onValueChange = { textFieldValue -> inputValue.value = textFieldValue }
        )
        Button(
            onClick = {
                memoList.add(
                    index = 0,
                    Memo(memoList.size, inputValue.value)
                )
                inputValue.value = ""
                // 버튼 클릭시 count 를 증가 시킴(갱신)
                count++
            },
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
        ) {
            // 버튼의 텍스트 뒤에 숫자를 추가한 형태
            // button 을 클릭하면 count 가 변화하고 이를 사용하는 Text는 recomposition 됨
            Text("ADD\n$count")
            // 버튼 클릭시 count 를 증가 시킴(갱신)
            // 숫자를 읽고 -> 값을 변경 시키는 구조
            // 리컴포지션 스코프내에 있기 때문에 count ++ 도 호출됨
            // 리컴포지션은 리컴포지션의 함수에만 적용되는게 아니라 범위에 걸쳐서 적용
            // count++
            // 증가 -> 리컴포지션 -> 증가 리컴포지션 -> 무한으로 증가, 리컴포지션(무한루프)
            // 리컴포지션 스코프내에 리컴포지션 대상이 존재할 경우 무한루프가 발생
            // 이 컴포지션 스코프 내에서 사용하는 값을 직접 쓰는 경우 (읽는 경우x) 발생
            // 역방향 쓰기 -> 읽힌 이후에 쓰는 것을 의미, 쓰면 안됨
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColumnScope.MemoList(onClickAction: (Int) -> Unit, memoList: SnapshotStateList<Memo>) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
    ) {
        items(
            items = memoList,
            key = { it.id }
        ) { memo ->
            Card(
                modifier = Modifier
                    .height(100.dp)
                    .background(Color.White)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                    .fillMaxWidth(),
                backgroundColor = Color.LightGray,
                onClick = {
                    onClickAction(memo.id)
                }
            ) {
                Text(
                    text = memo.text,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
