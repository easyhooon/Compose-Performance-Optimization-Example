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
            },
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
        ) {
            Text("ADD")
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
        // items 의 key 가 없어서 아이템이 새로 추가될때마다 리스트의 모든 요소들이 리컴포지션되는 것을 확인할 수 있음(Layout Inspector를 통해)
        // key 는 람다로 지정
        // Column 에서는 아이템을 감싸는 key 를 지정해주면 같은 효과를 낼 수 있음
        // for (memo in memoList) {
        //     key(memo.id) {
        //        Card()
        //     }
        // }
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
