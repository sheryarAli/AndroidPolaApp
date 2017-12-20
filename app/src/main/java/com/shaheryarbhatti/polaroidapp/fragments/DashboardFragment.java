package com.shaheryarbhatti.polaroidapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.adapters.GenericAdapter;
import com.shaheryarbhatti.polaroidapp.dataclasses.Post;
import com.shaheryarbhatti.polaroidapp.utilities.Util;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView dashboardRecylcerView;
    private GenericAdapter<Post> postAdapter;
    private ArrayList<Post> postList;
    private LinearLayoutManager linearLayoutManager;
    private String dummyString = "{“posts”: [ { \"userType\": 1, \"userName\": “jason\", \"userPicture”:”Jason” , \"profession\": \"Artist\", \"sourceType\": 0, \"source\": “boat”, \"titleText\": \"test\", \"postDate\": “09-12-2017”, \"postLikes\": 25,  \"Comments\": [ { \"userName\" : \"Clarke\", \"userPicture\" : “clarke”, \"profession\": “Designer” , \"commentDate\" : \" \", \"commentText\" : \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.”}],\"Made\":[ { \"madeSource\" : \"\", \"madeLikes\": 30, \"madeComments\" : \"\"} ]} ]}";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        iniilizing post array list
        postList = new ArrayList<>();
//        DummyData dummyData = new Gson().fromJson(dummyString, DummyData);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dashboardRecylcerView = (RecyclerView) view.findViewById(R.id.dashboardRecylcerView);


    }


    private void prepareDashboardRecylcerView() {
        dashboardRecylcerView.setLayoutManager(linearLayoutManager);
        //TODO popuplate postList
        /*postList*/
        postAdapter = new GenericAdapter<Post>(getContext(), postList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(getContext()).inflate(R.layout.row_list_item, parent, false);
                view.setOnClickListener(this);
                ItemViewHolder viewHolder = new ItemViewHolder(view);
                return viewHolder;
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Post val, int position) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.profileImageView.setImageBitmap(Util.getBitmapFromResource(getContext(), val.getUserProfilePicture()));
                if (val.getSourceType() == 1) {
                    itemViewHolder.postImageView.setImageBitmap(Util.getBitmapFromResource(getContext(), val.getSource()));

                }

            }

            @Override
            public void onItemClick(View view) {

            }
        };
        dashboardRecylcerView.setAdapter(postAdapter);


    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private CardView layoutRoot;
        private ImageView profileImageView, postImageView, madeImageView, commentImageView, likeImageView;
        private TextView professoinText, personNameText,
                durationText, commentText, madeText,
                titleText, likeText;

        public ItemViewHolder(View itemView) {
            super(itemView);
            layoutRoot = (CardView) itemView;
            profileImageView = (ImageView) layoutRoot.findViewById(R.id.profileImageView);
            postImageView = (ImageView) layoutRoot.findViewById(R.id.postImageView);
            madeImageView = (ImageView) layoutRoot.findViewById(R.id.madeImageView);
            commentImageView = (ImageView) layoutRoot.findViewById(R.id.commentImageView);
            likeImageView = (ImageView) layoutRoot.findViewById(R.id.likeImageView);
            professoinText = (TextView) layoutRoot.findViewById(R.id.professoinText);
            personNameText = (TextView) layoutRoot.findViewById(R.id.personNameText);
            durationText = (TextView) layoutRoot.findViewById(R.id.durationText);
            commentText = (TextView) layoutRoot.findViewById(R.id.commentText);
            madeText = (TextView) layoutRoot.findViewById(R.id.madeText);
            titleText = (TextView) layoutRoot.findViewById(R.id.titleText);
            likeText = (TextView) layoutRoot.findViewById(R.id.likeText);

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
