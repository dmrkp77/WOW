package com.mobile.fm.main;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobile.fm.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mobile.fm.exerciseboard.PostInfo;
import com.mobile.fm.main.search.SearchAdapter;
import com.mobile.fm.exerciseboard.listener.OnPostListener;

public class ActionSearch extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter searchAdapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<PostInfo> arraylist;
    private boolean updating;
    private boolean topScrolled;
    private ArrayList<PostInfo> list;
    ContentActivity activity;

    public ActionSearch(Context context) {
        this.activity = (ContentActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        arraylist = new ArrayList<>();
        list = new ArrayList<>();
        searchAdapter = new SearchAdapter(getActivity(), arraylist);
        searchAdapter.setOnPostListener(onPostListener);
        final RecyclerView recyclerView = view.findViewById(R.id.search_recyclerview);
//        view.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        editSearch = view.findViewById(R.id.editSearch);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(searchAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();

                if(newState == 1 && firstVisibleItemPosition == 0){
                    topScrolled = true;
                }
                if(newState == 0 && topScrolled){
                    postsUpdate(true);
                    topScrolled = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                if(totalItemCount - 3 <= lastVisibleItemPosition && !updating){
                    postsUpdate(false);
                }

                if(0 < firstVisibleItemPosition){
                    topScrolled = false;
                }
            }
        });

        postsUpdate(false);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        return view;
    }
        // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        arraylist.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
//            arraylist.addAll(list);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < list.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (list.get(i).getTitle().toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    arraylist.add(list.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        searchAdapter.notifyDataSetChanged();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause(){
        super.onPause();
        searchAdapter.playerStop();
    }
    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            arraylist.remove(postInfo);
            searchAdapter.notifyDataSetChanged();

            Log.e("로그: ","삭제 성공");
        }
        @Override
        public void onModify() {
            Log.e("로그: ","수정 성공");
        }
    };
    private void postsUpdate(final boolean clear) {
        updating = true;
        Date date = list.size() == 0 || clear ? new Date() : list.get(list.size() - 1).getCreatedAt();
        CollectionReference collectionReference = firebaseFirestore.collection("posts");
        collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).whereLessThan("createdAt", date).limit(10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){
                                list.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("so", document.getId() + " => " + document.getData());
                                list.add(new PostInfo(
                                        document.getData().get("title").toString(),
                                        (ArrayList<String>) document.getData().get("contents"),
                                        (ArrayList<String>) document.getData().get("formats"),
                                        document.getData().get("publisher").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getId(),
                                        document.getData().get("nid").toString()));
                            }
                        } else {
                            Log.d("so", "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }


    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }
}

//    ContentActivity activity;
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    public ActionSearch(Context context) {
//        this.activity = (ContentActivity) context;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
//        editSearch = (EditText) viewGroup.findViewById(R.id.editSearch);
//        listView = (RecyclerView) viewGroup.findViewById(R.id.search_recyclerview);
//        //searchbtn=(Button) viewGroup.findViewById(R.id.searchbutton);
////        searchbtn.setOnClickListener(onClickListener);
//        // 리스트를 생성한다.
//        list = new ArrayList<String>();
//
//        // 검색에 사용할 데이터을 미리 저장한다.
//        settingList();
//
//        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
//        arraylist = new ArrayList<>();
//        arraylist.addAll(list);
//        list.clear();
//
//        // 리스트에 연동될 아답터를 생성한다.
//        adapter = new SearchAdapter(list, getContext());
//
//        // 리스트뷰에 아답터를 연결한다.
//        listView.setAdapter(adapter);
//
//        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
//        editSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                // input창에 문자를 입력할때마다 호출된다.
//                // search 메소드를 호출한다.
//                String text = editSearch.getText().toString();
//                search(text);
//            }
//        });
//
//        return viewGroup;
//    }
////        View.OnClickListener onClickListener = new View.OnClickListener() {
////        @Override
////        public void onClick(View v) {
////            switch (v.getId()) {
////                case R.id.searchbutton:
////                    myStartActivity(SearchMainActivity.class);
////                    break;
////            }
////        }
////    };
////    private void myStartActivity(Class c) {
////        Intent intent = new Intent(getActivity(), c);
////        startActivityForResult(intent, 0);
////    }
//
//    // 검색을 수행하는 메소드
//    public void search(String charText) {
//
//        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
//        list.clear();
//
//        // 문자 입력이 없을때는 모든 데이터를 보여준다.
//        if (charText.length() == 0) {
//            //list.addAll(arraylist);
//        }
//        // 문자 입력을 할때..
//        else {
//            // 리스트의 모든 데이터를 검색한다.
//            for (int i = 0; i < arraylist.size(); i++) {
//                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
//                if (arraylist.get(i).toLowerCase().contains(charText)) {
//                    // 검색된 데이터를 리스트에 추가한다.
//                    list.add(arraylist.get(i));
//                }
//            }
//        }
//        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
//        adapter.notifyDataSetChanged();
//    }
//
//    // 검색에 사용될 데이터를 리스트에 추가한다.
//    private void settingList() {
////        db.collection("post")
////                .get()
////                .addOnSuccessListener()
//
//
//    }
//}
