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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.GsonBuilder;
import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.adapters.GenericAdapter;
import com.shaheryarbhatti.polaroidapp.dataclasses.Post;
import com.shaheryarbhatti.polaroidapp.dataclasses.ServerPost;
import com.shaheryarbhatti.polaroidapp.preferences.LocalStoragePreferences;
import com.shaheryarbhatti.polaroidapp.utilities.EndlessRecyclerViewScrollListener;
import com.shaheryarbhatti.polaroidapp.utilities.UtilImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


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
    private GenericAdapter<ServerPost> postAdapter;
    //    private GenericAdapter<ServerPost> serverPostAdapter;
    private ArrayList<Post> postList;
    private ArrayList<ServerPost> serverPosts;
    private LinearLayoutManager linearLayoutManager;
    private String youtubeThumbnailUrl = "https://img.youtube.com/vi/";
    private UtilImage utilImage;
    private HashMap<String, Bitmap> imageMap = new HashMap<>();
    private ArrayList<Bitmap> profileBitmaps = new ArrayList<>();
    private final int HOME_FEED = 0;
    private final int FEATURED_FEED = 1;
    private final int TOP_FEED = 2;
    private int limit = 5, skip = 0, count = 0;
    private LocalStoragePreferences preferences;
    private long currentTime;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);


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
        preferences = new LocalStoragePreferences(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        currentTime = Calendar.getInstance().getTimeInMillis();

//        iniilizing post array list
        postList = new ArrayList<>();
        serverPosts = new ArrayList<>();
        /*switch (mParam1) {
            case 0:
                postList.addAll(new DummyData().getDashboardPosts(getContext()));
                break;
            case 1:
                postList.addAll(new DummyData().getFeaturePosts(getContext()));
                break;
            case 2:
                postList.addAll(new DummyData().getPoplularPosts(getContext()));
                break;
        }*/

//        loadBitmapImages();
        loadMore();
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
        Log.d(TAG, "populatePostList: postFeedUrl: " + postFeedUrl);
        String authorization = preferences.getToken();
        Log.d(TAG, "populatePostList: token: " + authorization);
        Log.d(TAG, "populatePostList: limit: " + limit + " skip: " + skip);
        AndroidNetworking.get(postFeedUrl)
                .addHeaders("Authorization", authorization)
                .addQueryParameter("limit", limit + "")
                .addQueryParameter("skip", skip + "")

                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isSuccess = jsonObject.getBoolean("success");
                            if (isSuccess) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
//                                count = jsonObject.getInt("count");
                                skip += jsonArray.length();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ServerPost post = new GsonBuilder().create().fromJson(jsonArray.getJSONObject(i).toString(), ServerPost.class);
                                    Log.d(TAG, "onResponse: postId: " + post.getPostId());
                                    serverPosts.add(post);
                                }
                                postAdapter.notifyDataSetChanged();
                            }

                            /*String postId = dataObject.getString("_id");
                            boolean isFeatured = dataObject.getBoolean("isFeatured");
                            String title = dataObject.getString("title");
                            String type = dataObject.getString("type");
                            String createdAt = dataObject.getString("created_at");*/


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void loadMore() {
        populatePostList(mParam1);
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


    private void onPostViewClicked(View view, String postId) {
        Log.d(TAG, "onPostViewClicked: for debugging");
        onButtonPressed(postId);

    }

    private void onLikeViewClicked(View view) {
        Toast.makeText(getContext(), "Perform Like", Toast.LENGTH_SHORT).show();

    }

    private void onCommentViewClicked(View view, String postId) {
        onButtonPressed(postId);

    }

    private void onMadeViewClicked(View view, String postId) {
        onButtonPressed(postId);
    }


    private void prepareDashboardRecylcerView() {
        dashboardRecylcerView.setLayoutManager(linearLayoutManager);
        dashboardRecylcerView.setHasFixedSize(true);

        postAdapter = new GenericAdapter<ServerPost>(getContext(), serverPosts) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(getContext()).inflate(R.layout.row_list_item, parent, false);
                view.setOnClickListener(this);
                ItemViewHolder viewHolder = new ItemViewHolder(view);
                return viewHolder;
            }

            @Override
            public void onBindData(final RecyclerView.ViewHolder holder, final ServerPost val, final int position) {
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

//                itemViewHolder.profileImageView.setImageBitmap(profileBitmaps.get(position));
//                itemViewHolder.professoinText.setText(val.getProfession());
                if (val.getType().equalsIgnoreCase("video")) {
                    itemViewHolder.playIV.setVisibility(View.VISIBLE);
                } else {
                    itemViewHolder.playIV.setVisibility(View.GONE);
                }


/*                try {

                    long postTime = simpleDateFormat.parse(val.getCreatedAt()).getTime()
                    long diff = currentTime - postTime;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

                itemViewHolder.titleText.setText(val.getTitle());
                itemViewHolder.personNameText.setText(val.getUser().getName());
//                itemViewHolder.durationText.setText(val.getPostDuration() + " ago");
                itemViewHolder.likeText.setText(val.getLikes() + " Likes");
                itemViewHolder.madeText.setText("0");
                itemViewHolder.commentText.setText(val.getComments() + " Comments");
                if (preferences.getUserId().equalsIgnoreCase(val.getUser().getUserId())) {
                    itemViewHolder.subscribeBtn.setVisibility(View.GONE);
                }


                itemViewHolder.postImageView.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(val.getThumbnail()).resize(200, 200).into(itemViewHolder.postImageView);
//                itemViewHolder.postImageView.setImageBitmap(imageMap.get(val.getSource()));
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
                        Log.d(TAG, "onClick: val.getPostId(): " + val.getPostId());
                        onPostViewClicked(v, val.getPostId());
                    }
                });
                itemViewHolder.madeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: val.getPostId(): " + val.getPostId());
                        onMadeViewClicked(v, val.getPostId());
                    }
                });

                itemViewHolder.commentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: val.getPostId(): " + val.getPostId());
                        onCommentViewClicked(v, val.getPostId());
                    }
                });

                /*

                itemViewHolder.likeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLikeViewClicked(v);
                    }
                });

                */

            }


            @Override
            public void onItemClick(View view) {

            }


        };
        dashboardRecylcerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMore();
            }
        });

//        postAdapter.setHasStableIds(true);
        dashboardRecylcerView.setAdapter(postAdapter);


    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private View layoutRoot, socialContainer;
        private com.github.siyamed.shapeimageview.CircularImageView profileImageView;
        private ImageView postImageView, madeImageView, commentImageView, likeImageView, playIV;
        private TextView professoinText, personNameText,
                durationText, commentText, madeText,
                titleText, likeText;
        private Button subscribeBtn;


        public ItemViewHolder(View itemView) {
            super(itemView);
            layoutRoot = itemView;
            profileImageView = layoutRoot.findViewById(R.id.profileImageView);
            playIV = layoutRoot.findViewById(R.id.playIV);
            postImageView = layoutRoot.findViewById(R.id.postImageView);
            professoinText = layoutRoot.findViewById(R.id.professoinText);
            personNameText = layoutRoot.findViewById(R.id.personNameText);
            durationText = layoutRoot.findViewById(R.id.durationText);
            titleText = layoutRoot.findViewById(R.id.titleText);
            socialContainer = layoutRoot.findViewById(R.id.socialContainer);
            madeImageView = socialContainer.findViewById(R.id.madeBtn);
            commentImageView = socialContainer.findViewById(R.id.commentBtn);
            likeImageView = socialContainer.findViewById(R.id.likeBtn);
            commentText = socialContainer.findViewById(R.id.commentText);
            madeText = socialContainer.findViewById(R.id.madeText);
            likeText = socialContainer.findViewById(R.id.likeText);
            subscribeBtn = socialContainer.findViewById(R.id.subscribeBtn);
            postImageView.setVisibility(View.VISIBLE);
            layoutRoot.findViewById(R.id.btnContainer).setVisibility(View.GONE);
//            layoutRoot.findViewById(R.id.frame_fragment).setVisibility(View.GONE);


        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String postId) {
        if (mListener != null) {
            Bundle args = new Bundle();
            args.putString("postId", postId);
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
