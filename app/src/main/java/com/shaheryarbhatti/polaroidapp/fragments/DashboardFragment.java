package com.shaheryarbhatti.polaroidapp.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.adapters.GenericAdapter;
import com.shaheryarbhatti.polaroidapp.dataclasses.DummyData;
import com.shaheryarbhatti.polaroidapp.dataclasses.Post;
import com.shaheryarbhatti.polaroidapp.utilities.UtilImage;

import java.util.ArrayList;
import java.util.HashMap;


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
    private final String TAG = "DashboardFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView dashboardRecylcerView;
    private GenericAdapter<Post> postAdapter;
    private ArrayList<Post> postList;
    private LinearLayoutManager linearLayoutManager;
    private String youtubeThumbnailUrl = "https://img.youtube.com/vi/";
    private UtilImage utilImage;
    private HashMap<String, Bitmap> imageMap = new HashMap<>();
    private ArrayList<Bitmap> profileBitmaps = new ArrayList<>();
    private final int HOME_FEED = 0;
    private final int FEATURED_FEED = 1;
    private final int TOP_FEED = 2;


    // TODO: Rename and change types of parameters
    private int mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(int page) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utilImage = UtilImage.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }


//        iniilizing post array list
        postList = new ArrayList<>();
        switch (mParam1) {
            case 0:
                postList.addAll(new DummyData().getDashboardPosts(getContext()));
                break;
            case 1:
                postList.addAll(new DummyData().getFeaturePosts(getContext()));
                break;
            case 2:
                postList.addAll(new DummyData().getPoplularPosts(getContext()));
                break;
        }

        loadBitmapImages();
        Log.d(TAG, "onCreate: For Debugging");
    }

    private void populatePostList(int i) {
        String postFeedUrl = "";
        switch (i) {

            case HOME_FEED:
                postFeedUrl = getResources().getString(R.string.home_post_feed_test_url);
                break;

            case FEATURED_FEED:
                postFeedUrl = getResources().getString(R.string.featured_post_feed_test_url);
                break;
            case TOP_FEED:
                postFeedUrl = getResources().getString(R.string.top_post_feed_test_url);
                break;


        }
        String authorization = "";
        AndroidNetworking.post(postFeedUrl)
                .addHeaders("Authorization", authorization)

                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                    }
                });
    }

    private void handleSubscribe(String userId) {
        String subscribeUrl = "http://35.178.99.1:3000/post/" + userId + "/subscribe";
        String authorization = "";
        AndroidNetworking.post(subscribeUrl)
                .addHeaders("Authorization", authorization)

                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                    }
                });

    }

    private void handleLike(String userId) {
        String subscribeUrl = "http://35.178.99.1:3000/post/" + userId + "/like";
        String authorization = "";
        AndroidNetworking.post(subscribeUrl)
                .addHeaders("Authorization", authorization)

                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                    }
                });

    }

    private void loadBitmapImages() {
        for (Post post : postList) {
//            if (post.getSourceType() == 1) {
            imageMap.put(post.getSource(), utilImage.loadScaledDownBitmapForDisplay(getContext(),
                    utilImage.getDrawableId(getContext(), post.getSource()), 200, 200));
//            }

//            if (post.getSourceType() == 2){
//            imageMap.put(post.getSource(), utilImage.createVideoThumbNail(post.getSource()));
//            }
            profileBitmaps.add(utilImage.loadScaledDownBitmapForDisplay(getContext(),
                    utilImage.getDrawableId(getContext(), post.getUserPicture()), 50, 50));
        }
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

        prepareDashboardRecylcerView();

    }


    private void onPostViewClicked(View view, Post post) {
        Log.d(TAG, "onPostViewClicked: for debugging");
        onButtonPressed(post);

    }

    private void onLikeViewClicked(View view) {
        Toast.makeText(getContext(), "Perform Like", Toast.LENGTH_SHORT).show();

    }

    private void onCommentViewClicked(View view, Post post) {
        onButtonPressed(post);

    }

    private void onMadeViewClicked(View view, Post post) {
        onButtonPressed(post);
    }


    private void prepareDashboardRecylcerView() {
        dashboardRecylcerView.setLayoutManager(linearLayoutManager);
        dashboardRecylcerView.setHasFixedSize(true);

        postAdapter = new GenericAdapter<Post>(getContext(), postList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(getContext()).inflate(R.layout.row_list_item, parent, false);
                view.setOnClickListener(this);
                ItemViewHolder viewHolder = new ItemViewHolder(view);
                return viewHolder;
            }

            @Override
            public void onBindData(final RecyclerView.ViewHolder holder, final Post val, int position) {
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

                itemViewHolder.profileImageView.setImageBitmap(profileBitmaps.get(position));
                itemViewHolder.professoinText.setText(val.getProfession());
                itemViewHolder.titleText.setText(val.getTitleText());
                itemViewHolder.personNameText.setText(val.getUserName());
                itemViewHolder.durationText.setText(val.getPostDuration() + " ago");
                itemViewHolder.likeText.setText(val.getPostLikes() + " Likes");
                itemViewHolder.madeText.setText(val.getMade().size() + " Mades");
                itemViewHolder.commentText.setText(val.getComments().size() + " Comments");

                itemViewHolder.postImageView.setVisibility(View.VISIBLE);
                itemViewHolder.postImageView.setImageBitmap(imageMap.get(val.getSource()));
//                if (val.getSourceType() == 1) {

//                    itemViewHolder.postImageView.setVisibility(View.VISIBLE);
                    /*UtilImage.setPicFromDrawableResuource(context, itemViewHolder.postImageView,
                            UtilImage.getDrawableId(context, val.getSource()));*/
//                    itemViewHolder.postImageView.setImageBitmap(imageMap.get(val.getSource()));
//                    utilImage.loadImageWithPicasso(context, itemViewHolder.postImageView, val.getSource());

//                }
                /*if (val.getSourceType() == 2) {
                    String thumbnailImageUrl = youtubeThumbnailUrl + val.getSource().split("=")[1] + "/0.jpg";
                    Log.d(TAG, "onBindData: thumbnailImageUrl: " + thumbnailImageUrl);
                    utilImage.loadThumbnailWithPicasso(context, itemViewHolder.postImageView, thumbnailImageUrl);

                }*/

                itemViewHolder.postImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPostViewClicked(v, val);
                    }
                });

                itemViewHolder.likeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLikeViewClicked(v);
                    }
                });

                itemViewHolder.madeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onMadeViewClicked(v, val);
                    }
                });

                itemViewHolder.commentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentViewClicked(v, val);
                    }
                });

            }


            @Override
            public void onItemClick(View view) {

            }
        };
//        postAdapter.setHasStableIds(true);
        dashboardRecylcerView.setAdapter(postAdapter);


    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private View layoutRoot, socialContainer;
        private com.github.siyamed.shapeimageview.CircularImageView profileImageView;
        private ImageView postImageView, madeImageView, commentImageView, likeImageView;
        private TextView professoinText, personNameText,
                durationText, commentText, madeText,
                titleText, likeText;


        public ItemViewHolder(View itemView) {
            super(itemView);
            layoutRoot = itemView;
            profileImageView = (com.github.siyamed.shapeimageview.CircularImageView) layoutRoot.findViewById(R.id.profileImageView);
            postImageView = (ImageView) layoutRoot.findViewById(R.id.postImageView);
            professoinText = (TextView) layoutRoot.findViewById(R.id.professoinText);
            personNameText = (TextView) layoutRoot.findViewById(R.id.personNameText);
            durationText = (TextView) layoutRoot.findViewById(R.id.durationText);
            titleText = (TextView) layoutRoot.findViewById(R.id.titleText);
            socialContainer = layoutRoot.findViewById(R.id.socialContainer);
            madeImageView = (ImageView) socialContainer.findViewById(R.id.madeBtn);
            commentImageView = (ImageView) socialContainer.findViewById(R.id.commentBtn);
            likeImageView = (ImageView) socialContainer.findViewById(R.id.likeBtn);
            commentText = (TextView) socialContainer.findViewById(R.id.commentText);
            madeText = (TextView) socialContainer.findViewById(R.id.madeText);
            likeText = (TextView) socialContainer.findViewById(R.id.likeText);
            postImageView.setVisibility(View.VISIBLE);
            layoutRoot.findViewById(R.id.btnContainer).setVisibility(View.GONE);
            layoutRoot.findViewById(R.id.frame_fragment).setVisibility(View.GONE);


        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Post post) {
        if (mListener != null) {
            Bundle args = new Bundle();
            args.putSerializable("post", post);
            mListener.onDashboardFragmentInteraction(args);
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
        void onDashboardFragmentInteraction(Bundle args);
    }


}
