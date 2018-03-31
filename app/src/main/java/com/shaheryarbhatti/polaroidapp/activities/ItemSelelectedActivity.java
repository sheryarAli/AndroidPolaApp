package com.shaheryarbhatti.polaroidapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.shaheryarbhatti.polaroidapp.Config;
import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.adapters.GenericAdapter;
import com.shaheryarbhatti.polaroidapp.dataclasses.Comment;
import com.shaheryarbhatti.polaroidapp.dataclasses.Made;
import com.shaheryarbhatti.polaroidapp.dataclasses.Post;
import com.shaheryarbhatti.polaroidapp.utilities.UtilImage;

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
    private ArrayList<Made> madeList;
    private GenericAdapter<Comment> commentGenericAdapter;
    private GenericAdapter<Made> madeGenericAdapter;
    private LinearLayoutManager commentLayoutManager;
    private GridLayoutManager madeLayoutManager;
    private View postContainer, commentSectionView, madeSectionView, socialContainer, commentSectionSubView, btnContainer;
    private TextView commentSectionText, madeSectionText;
    private Button drawBtn;
    private Post post;
    private final int MADE_COLUMNS = 3;
    private FrameLayout videoLayout;
    private ScrollView scroll;
    private String videoSource;
    private UtilImage utilImage;
    private YouTubePlayer yPlayer = null;
    private boolean mAutoRotation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected);
        initViews();
        utilImage = UtilImage.getInstance();
//        mAutoRotation = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Stencil");
        commentLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        madeLayoutManager = new GridLayoutManager(this, MADE_COLUMNS);
        prepareViews();


    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        postContainer = findViewById(R.id.postContainer);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMarginEnd(0);
        cardParams.setMarginStart(0);
        ((CardView) postContainer).setLayoutParams(cardParams);
        titleText = (TextView) postContainer.findViewById(R.id.titleText);
        profileImageView = (com.github.siyamed.shapeimageview.CircularImageView) postContainer.findViewById(R.id.profileImageView);
        personNameText = (TextView) postContainer.findViewById(R.id.personNameText);
        professoinText = (TextView) postContainer.findViewById(R.id.professoinText);
        postImageView = (ImageView) postContainer.findViewById(R.id.postImageView);
        durationText = (TextView) postContainer.findViewById(R.id.durationText);
        socialContainer = postContainer.findViewById(R.id.socialContainer);
        likeText = (TextView) socialContainer.findViewById(R.id.likeText);
        commentText = (TextView) socialContainer.findViewById(R.id.commentText);
        madeText = (TextView) socialContainer.findViewById(R.id.madeText);

        btnContainer = findViewById(R.id.btnContainer);
        commentSectionView = findViewById(R.id.commentSectionView);
        madeSectionView = findViewById(R.id.madeSectionView);

        drawBtn = (Button) postContainer.findViewById(R.id.drawBtn);
        commentSectionText = (TextView) commentSectionView.findViewById(R.id.sectionText);

        commentImageView = (ImageView) commentSectionView.findViewById(R.id.sectionImageView);
        commentSectionSubView = commentSectionView.findViewById(R.id.commentSectionSubView);
        madeSectionText = (TextView) madeSectionView.findViewById(R.id.sectionText);
        madeImageView = (ImageView) madeSectionView.findViewById(R.id.sectionImageView);

        commentRecyclerView = (RecyclerView) commentSectionView.findViewById(R.id.sectoinsRecyclerView);
        madeRecyclerView = (RecyclerView) madeSectionView.findViewById(R.id.sectoinsRecyclerView);
        drawBtn.setOnClickListener(this);
        toolbar.findViewById(R.id.logoImageView).setVisibility(View.GONE);
        videoLayout = (FrameLayout) postContainer.findViewById(R.id.frame_fragment);

        ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
        scroll.setFocusableInTouchMode(true);
        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

    }

    private void prepareViews() {
        commentSectionText.setText("Comments");
        madeSectionText.setText("Mades");
        commentSectionSubView.setVisibility(View.VISIBLE);
        ((com.github.siyamed.shapeimageview.CircularImageView) commentSectionSubView.findViewById(R.id.userImageView)).setImageBitmap(utilImage.getBitmap(this, "martin"));
        commentImageView.setImageBitmap(utilImage.getBitmap(this, "ic_textsms"));
        commentImageView.setColorFilter(ContextCompat.getColor(this, R.color.light_blue_400));
        madeImageView.setImageBitmap(utilImage.getBitmap(this, "ic_pencil"));
        Bundle data = getIntent().getBundleExtra("post");
        post = (Post) data.getSerializable("post");
        titleText.setText(post.getTitleText());
        durationText.setText(post.getPostDuration() + " ago");
        professoinText.setText(post.getProfession());
        personNameText.setText(post.getUserName());

        profileImageView.setImageBitmap(utilImage.getBitmapFromDrawableResuource(this,
                utilImage.getDrawableId(this, post.getUserPicture())));
        commentList = post.getComments();
        madeList = post.getMade();
        likeText.setText(post.getPostLikes() + " Likes");
        commentText.setText(commentList.size() + " Comments");
        madeText.setText(madeList.size() + " Mades");
        Log.d(TAG, "prepareViews: for Debugging");
        prepareMadeRecyclerView();
        prepareCommentRecylerView();
        if (post.getSourceType() == 1) {
            postImageView.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
            btnContainer.setVisibility(View.VISIBLE);
            postImageView.setImageBitmap(utilImage.loadScaledDownBitmapForDisplay(this,
                    utilImage.getDrawableId(this, post.getSource()), 200, 200));
        } else if (post.getSourceType() == 2) {
            postImageView.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
            postImageView.setImageBitmap(utilImage.loadScaledDownBitmapForDisplay(this,
                    utilImage.getDrawableId(this, post.getSource()), 200, 200));
            postImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String videoPath = utilImage.generateDownloadPath(post.getSource() + ".mp4");
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(videoPath), "video/mp4");
                    startActivity(intent);
                }
            });
//            setVideo();
            btnContainer.setVisibility(View.GONE);
        }


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
                viewHolder.userImageView.setImageBitmap(utilImage.getBitmap(context, val.getUserPicture()));
                viewHolder.userText.setText(val.getUserName());
                viewHolder.commentText.setText(val.getCommentText());
                viewHolder.timeText.setText(val.getCommentDuration() + " ago");


            }

            @Override
            public void onItemClick(View view) {

            }

        };
        commentRecyclerView.setAdapter(commentGenericAdapter);
    }

    private void prepareMadeRecyclerView() {
        madeRecyclerView.setLayoutManager(madeLayoutManager);
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
        madeRecyclerView.setAdapter(madeGenericAdapter);

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
        intent.putExtra("image", post.getSource());
        startActivity(intent);

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


    private void setVideo() {
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_fragment, youTubePlayerFragment);
        fragmentTransaction.commit();

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
