package com.shaheryarbhatti.polaroidapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.adapters.GenericAdapter;
import com.shaheryarbhatti.polaroidapp.dataclasses.Comment;
import com.shaheryarbhatti.polaroidapp.dataclasses.Made;
import com.shaheryarbhatti.polaroidapp.dataclasses.Post;
import com.shaheryarbhatti.polaroidapp.preferences.LocalStoragePreferences;
import com.shaheryarbhatti.polaroidapp.utilities.UtilImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Shahzore on 12/16/2017.
 */


public class ItemSelelectedActivity extends AppCompatActivity implements View.OnClickListener, YouTubePlayer.OnInitializedListener/*, YouTubePlayer.OnFullscreenListener*/ {

    /*@SuppressLint("InlinedApi")
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @SuppressLint("InlinedApi")
    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;*/

    private final String TAG = "ItemSelelectedActivity";
    private Toolbar toolbar;
    private TextView titleText, personNameText, professoinText, durationText, likeText, commentText, madeText;
    private ImageView postImageView, commentImageView, madeImageView;
    private RecyclerView commentRecyclerView, madeRecyclerView;
    private com.github.siyamed.shapeimageview.CircularImageView profileImageView;
    private ArrayList<Comment> commentList;
    //    private ArrayList<Made> madeList;
    private GenericAdapter<Comment> commentGenericAdapter;
    private GenericAdapter<Made> madeGenericAdapter;
    private LinearLayoutManager commentLayoutManager;
    private GridLayoutManager madeLayoutManager;
    private View postContainer, commentSectionView, madeSectionView, socialContainer, commentSectionSubView, btnContainer;
    private TextView commentSectionText, madeSectionText;
    private Button drawBtn, subscribeBtn;
    private Post post;
    private final int MADE_COLUMNS = 3;
    private ProgressBar loadingBar;
    private ScrollView scrollView;
    private EditText commentEdt;
    //    private VideoView videoLayout;
    private ScrollView scroll;
    private String videoSource;
    private UtilImage utilImage;
    private YouTubePlayer yPlayer = null;
    private boolean mAutoRotation = false;
    private LocalStoragePreferences preferences;
    private ImageView playIV;
    String imageUrl, userId, postId, authorization, userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected);
        preferences = new LocalStoragePreferences(this);
        initViews();
        utilImage = UtilImage.getInstance();
//        mAutoRotation = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Stencil");
        commentLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        madeLayoutManager = new GridLayoutManager(this, MADE_COLUMNS);
        postId = getIntent().getBundleExtra("post").getString("postId");
        authorization = preferences.getToken();
        userName = preferences.getName();
        Log.d(TAG, "onCreate: postId: " + postId);
        prepareViews();
        userId = preferences.getUserId();
        handlePostFetch(postId);


    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        postContainer = findViewById(R.id.postContainer);
        loadingBar = findViewById(R.id.loadingBar);
        scrollView = findViewById(R.id.scrollView);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMarginEnd(0);
        cardParams.setMarginStart(0);
        ((CardView) postContainer).setLayoutParams(cardParams);
        titleText = postContainer.findViewById(R.id.titleText);
        profileImageView = postContainer.findViewById(R.id.profileImageView);
        playIV = postContainer.findViewById(R.id.playIV);
        personNameText = postContainer.findViewById(R.id.personNameText);
        professoinText = postContainer.findViewById(R.id.professoinText);
        postImageView = postContainer.findViewById(R.id.postImageView);
        durationText = postContainer.findViewById(R.id.durationText);
        socialContainer = postContainer.findViewById(R.id.socialContainer);
        likeText = socialContainer.findViewById(R.id.likeText);
        commentText = socialContainer.findViewById(R.id.commentText);
        madeText = socialContainer.findViewById(R.id.madeText);
        subscribeBtn = socialContainer.findViewById(R.id.subscribeBtn);

        btnContainer = findViewById(R.id.btnContainer);
        commentSectionView = findViewById(R.id.commentSectionView);
        madeSectionView = findViewById(R.id.madeSectionView);

        drawBtn = postContainer.findViewById(R.id.drawBtn);
        commentSectionText = commentSectionView.findViewById(R.id.sectionText);

        commentImageView = commentSectionView.findViewById(R.id.sectionImageView);
        commentSectionSubView = commentSectionView.findViewById(R.id.commentSectionSubView);
        madeSectionText = madeSectionView.findViewById(R.id.sectionText);
        madeImageView = madeSectionView.findViewById(R.id.sectionImageView);

        commentRecyclerView = commentSectionView.findViewById(R.id.sectoinsRecyclerView);
        commentEdt = commentSectionView.findViewById(R.id.commentEdt);
        madeRecyclerView = madeSectionView.findViewById(R.id.sectoinsRecyclerView);
        drawBtn.setOnClickListener(this);
        subscribeBtn.setOnClickListener(this);
        commentEdt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handleComment(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
        toolbar.findViewById(R.id.logoImageView).setVisibility(View.GONE);
//        videoLayout = (VideoView) postContainer.findViewById(R.id.frame_fragment);

        ScrollView scroll = findViewById(R.id.scrollView);
        scroll.setFocusableInTouchMode(true);
        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

    }

    private void prepareViews() {
        commentSectionText.setText("Comments");
        madeSectionText.setText("Mades");
        commentSectionSubView.setVisibility(View.VISIBLE);
//        ((com.github.siyamed.shapeimageview.CircularImageView) commentSectionSubView.findViewById(R.id.userImageView)).setImageBitmap(utilImage.getBitmap(this, "martin"));
        commentImageView.setImageBitmap(utilImage.getBitmap(this, "ic_textsms"));
        commentImageView.setColorFilter(ContextCompat.getColor(this, R.color.light_blue_400));
        madeImageView.setImageBitmap(utilImage.getBitmap(this, "ic_pencil"));

        /*Bundle data = getIntent().getBundleExtra("post");
        post = (Post) data.getSerializable("post");
        titleText.setText(post.getTitleText());
        durationText.setText(post.getPostDuration() + " ago");
        professoinText.setText(post.getProfession());
        personNameText.setText(post.getUserName());*/

        /*profileImageView.setImageBitmap(utilImage.getBitmapFromDrawableResuource(this,
                utilImage.getDrawableId(this, post.getUserPicture())));*/
        commentList = new ArrayList<>();
//        madeList = post.getMade();
//        likeText.setText(post.getPostLikes() + " Likes");
//        commentText.setText(commentList.size() + " Comments");
//        madeText.setText(madeList.size() + " Mades");
        Log.d(TAG, "prepareViews: for Debugging");
//        prepareMadeRecyclerView();
        prepareCommentRecylerView();
        /*if (post.getSourceType() == 1) {
            postImageView.setVisibility(View.VISIBLE);
//            videoLayout.setVisibility(View.GONE);
            btnContainer.setVisibility(View.VISIBLE);
            postImageView.setImageBitmap(utilImage.loadScaledDownBitmapForDisplay(this,
                    utilImage.getDrawableId(this, post.getSource()), 200, 200));
        } else if (post.getSourceType() == 2) {
            final String videoPath = utilImage.generateDownloadPath(post.getSource() + ".mp4");
            postImageView.setVisibility(View.VISIBLE);
//            videoLayout.setVisibility(View.GONE);
            postImageView.setImageBitmap(utilImage.loadScaledDownBitmapForDisplay(this,
                    utilImage.getDrawableId(this, post.getSource()), 200, 200));
            postImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(videoPath), "video/mp4");
                    startActivity(intent);
                }
            });
            setVideo(videoPath);
            btnContainer.setVisibility(View.GONE);
        }*/


    }

    private void handleComment(final String commentStr) {
        Log.d(TAG, "handleComment: authorization: " + authorization + " postId: " + postId);
        String commentUrl = "http://35.178.99.1:3000/post/" + postId + "/comment/seed";

        AndroidNetworking.post(commentUrl)
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addHeaders("Authorization", authorization)
                .addBodyParameter("text", commentStr)

                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isSuccess = jsonObject.getBoolean("success");
                            if (isSuccess) {
                                Comment comment = new Comment();
                                comment.setCommentText(commentStr);
                                comment.setUserName(userName);
                                comment.setPostId(postId);
                                comment.setUserId(userId);
                                commentList.add(comment);
                                commentGenericAdapter.notifyDataSetChanged();
                                commentEdt.setText("");
                                hideKeyboard();

                                commentText.setText(commentList.size() + " comments");

                            }
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

    private void handleSubscribe(String userId) {
        String subscribeUrl = "http://35.178.99.1:3000/post/" + userId + "/subscribe";

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

    private void handleLike() {
        String subscribeUrl = "http://35.178.99.1:3000/post/" + postId + "/like";
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


    private void handlePostFetch(String postId) {
        scrollView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);
        String postUrl = "http://35.178.99.1:3000/post/" + postId;
        String authorization = preferences.getToken();
        AndroidNetworking.get(postUrl)
                .addHeaders("Authorization", authorization)

                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isSuccess = jsonObject.getBoolean("success");
                            if (isSuccess) {
                                scrollView.setVisibility(View.VISIBLE);
                                loadingBar.setVisibility(View.GONE);
                                final JSONObject dataObj = jsonObject.getJSONObject("data");
                                titleText.setText(dataObj.getString("title"));
                                JSONObject userObj = dataObj.getJSONObject("user");
                                personNameText.setText(userObj.getString("name"));
                                JSONArray commentJArray = dataObj.getJSONArray("comments");
                                JSONArray likes = dataObj.getJSONArray("likes");
                                commentText.setText(commentJArray.length() + " comments");
                                likeText.setText(likes.length() + " likes");
                                madeText.setText("0 mades");
                                if (!commentJArray.getJSONObject(0).toString().equals("{}")) {
                                    for (int i = 0; i < commentJArray.length(); i++) {
                                        JSONObject commentJObject = commentJArray.getJSONObject(i);
                                        Comment comment = new Comment();
                                        comment.setUserId(commentJObject.getString("user_id"));
                                        comment.setPostId(commentJObject.getString("post_id"));
                                        comment.setCommentId(commentJObject.getString("_id"));
                                        comment.setCommentText(commentJObject.getString("text"));
                                        JSONObject userJObject = commentJObject.getJSONObject("user");
                                        comment.setUserName(userJObject.getString("name"));
                                        commentList.add(comment);
                                        commentGenericAdapter.notifyDataSetChanged();
                                    }
                                }


                                if (preferences.getUserId().equalsIgnoreCase(userObj.getString("_id"))) {
                                    subscribeBtn.setVisibility(View.GONE);
                                }

                                if (dataObj.getString("type").equalsIgnoreCase("video")) {
                                    playIV.setVisibility(View.VISIBLE);
                                    btnContainer.setVisibility(View.GONE);
                                } else {
                                    imageUrl = dataObj.getString("source");
                                    playIV.setVisibility(View.GONE);
                                    btnContainer.setVisibility(View.VISIBLE);
                                }


                                Picasso.with(ItemSelelectedActivity.this).load(dataObj.getString("thumbnail")).resize(200, 200).into(postImageView);


                                playIV.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Intent intent = new Intent(ItemSelelectedActivity.this, VideoStreamingActivity.class);
                                            intent.putExtra("videoUrl", dataObj.getString("source"));
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
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

    private void prepareCommentRecylerView() {
        commentRecyclerView.setLayoutManager(commentLayoutManager);
        commentGenericAdapter = new GenericAdapter<Comment>(this, commentList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(ItemSelelectedActivity.this).inflate(R.layout.list_item_comments, parent, false);
                view.setOnClickListener(this);
                CommentViewHolder viewHolder = new CommentViewHolder(view);
                return viewHolder;
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Comment val, int position) {
                CommentViewHolder viewHolder = (CommentViewHolder) holder;
//                viewHolder.userImageView.setImageBitmap(utilImage.getBitmap(context, val.getUserPicture()));
                viewHolder.userText.setText(val.getUserName());
                viewHolder.commentText.setText(val.getCommentText());
//                viewHolder.timeText.setText(val.getCommentDuration() + " ago");


            }

            @Override
            public void onItemClick(View view) {

            }

        };
        commentRecyclerView.setAdapter(commentGenericAdapter);
    }

    private void prepareMadeRecyclerView() {
        /*madeRecyclerView.setLayoutManager(madeLayoutManager);
        madeGenericAdapter = new GenericAdapter<Made>(this, madeList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(ItemSelelectedActivity.this).inflate(R.layout.row_made_item, parent, false);
                view.setOnClickListener(this);
                MadeViewHolder viewHolder = new MadeViewHolder(view);
                return viewHolder;
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Made val, int position) {
                MadeViewHolder viewHolder = (MadeViewHolder) holder;
                viewHolder.madeImageView.setTag(position);
                utilImage.loadImageWithPicasso(context, viewHolder.madeImageView, val.getMadeSource());
                viewHolder.madeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = Integer.parseInt(v.getTag() + "");
                        moveToMadeActivity(madeGenericAdapter.getItem(position).getMadeSource());

                    }
                });


            }

            @Override
            public void onItemClick(View view) {


            }
        };
        madeRecyclerView.setAdapter(madeGenericAdapter);*/

    }

    private void moveToMadeActivity(String made) {
        Intent intent = new Intent(ItemSelelectedActivity.this, MadeActivity.class);
        intent.putExtra("made", made);
        startActivity(intent);


    }

    @Override
    public void onClick(View v) {
        if (v == drawBtn) {
            onDrawBtnClicked();
        }
    }


    private void onDrawBtnClicked() {
        Intent intent = new Intent(ItemSelelectedActivity.this, DrawingBoardActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        startActivity(intent);

    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private class CommentViewHolder extends RecyclerView.ViewHolder {
        private com.github.siyamed.shapeimageview.CircularImageView userImageView;
        private TextView userText, timeText, commentText;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userImageView = (com.github.siyamed.shapeimageview.CircularImageView) itemView.findViewById(R.id.userImageView);
            userText = (TextView) itemView.findViewById(R.id.userText);
            timeText = (TextView) itemView.findViewById(R.id.timeText);
            commentText = (TextView) itemView.findViewById(R.id.commentText);
        }
    }


    private class MadeViewHolder extends RecyclerView.ViewHolder {
        private ImageView madeImageView;
        private View rootView;


        public MadeViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            madeImageView = (ImageView) itemView.findViewById(R.id.madeImageView);

        }
    }


    private void setVideo(String URL) {
//        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//        youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_fragment, youTubePlayerFragment);
//        fragmentTransaction.commit();
        /*Uri uri = Uri.parse(URL); //Declare your url here.


        videoLayout.setMediaController(new MediaController(this));
        videoLayout.setVideoURI(uri);
        videoLayout.requestFocus();
        videoLayout.start();*/
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
        );


        if (!wasRestored) {
            youTubePlayer.cueVideo(videoSource);
        }/*else {
            youTubePlayer.play();
        }*/
        /*
        yPlayer = youTubePlayer;
        youTubePlayer.setOnFullscreenListener(ItemSelelectedActivity.this);
        if (mAutoRotation) {
            youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                    | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        } else {
            youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        }

        if (!wasRestored) {
            yPlayer.loadVideo(videoSource);
        }*/
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
        final int REQUEST_CODE = 1;

        if (error.isUserRecoverableError()) {
            error.getErrorDialog(this, REQUEST_CODE).show();
        } else {
            String errorMessage = String.format("There was an error initializing the YoutubePlayer (%1$s)", error.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    /*@Override
    public void onFullscreen(boolean b) {
        if (b) {
            setRequestedOrientation(LANDSCAPE_ORIENTATION);
        } else {
            setRequestedOrientation(PORTRAIT_ORIENTATION);
        }
    }*/
}
