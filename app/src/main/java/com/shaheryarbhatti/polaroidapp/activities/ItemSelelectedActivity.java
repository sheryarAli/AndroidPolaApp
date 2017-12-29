package com.shaheryarbhatti.polaroidapp.activities;

import android.content.Intent;
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
import com.mikhaellopez.circularimageview.CircularImageView;
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


public class ItemSelelectedActivity extends AppCompatActivity implements View.OnClickListener, YouTubePlayer.OnInitializedListener {


    private final String TAG = "ItemSelelectedActivity";
    private Toolbar toolbar;
    private TextView titleText, personNameText, professoinText, durationText;
    private ImageView postImageView, commentImageView, madeImageView;
    private RecyclerView commentRecyclerView, madeRecyclerView;
    private CircularImageView profileImageView;
    private ArrayList<Comment> commentList;
    private ArrayList<Made> madeList;
    private GenericAdapter<Comment> commentGenericAdapter;
    private GenericAdapter<Made> madeGenericAdapter;
    private LinearLayoutManager commentLayoutManager;
    private GridLayoutManager madeLayoutManager;
    private View postContainer, commentSectionView, madeSectionView, socialContainer;
    private TextView commentText, madeText;
    private Button drawBtn;
    private Post post;
    private final int MADE_COLUMNS = 3;
    private FrameLayout videoLayout;
    private ScrollView scroll;
    private String videoSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        profileImageView = (CircularImageView) postContainer.findViewById(R.id.profileImageView);
        personNameText = (TextView) postContainer.findViewById(R.id.personNameText);
        professoinText = (TextView) postContainer.findViewById(R.id.professoinText);
        postImageView = (ImageView) postContainer.findViewById(R.id.postImageView);
        durationText = (TextView) postContainer.findViewById(R.id.durationText);
        commentSectionView = findViewById(R.id.commentSectionView);
        madeSectionView = findViewById(R.id.madeSectionView);

        drawBtn = (Button) postContainer.findViewById(R.id.drawBtn);
        commentText = (TextView) commentSectionView.findViewById(R.id.sectionText);
        commentImageView = (ImageView) commentSectionView.findViewById(R.id.sectionImageView);
        madeText = (TextView) madeSectionView.findViewById(R.id.sectionText);
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
        commentText.setText("Comments");
        madeText.setText("Mades");
        commentImageView.setImageBitmap(UtilImage.getBitmap(this, "ic_textsms"));
        commentImageView.setColorFilter(ContextCompat.getColor(this, R.color.light_blue_400));
        madeImageView.setImageBitmap(UtilImage.getBitmap(this, "ic_pencil"));
        Bundle data = getIntent().getBundleExtra("post");
        post = (Post) data.getSerializable("post");
        titleText.setText(post.getTitleText());
        durationText.setText(post.getPostDuration() + " ago");
        professoinText.setText(post.getProfession());
        personNameText.setText(post.getUserName());
        profileImageView.setImageBitmap(UtilImage.getBitmap(this, post.getUserPicture()));
        commentList = post.getComments();
        madeList = post.getMade();
        Log.d(TAG, "prepareViews: for Debugging");
        prepareMadeRecyclerView();
        prepareCommentRecylerView();
        if (post.getSourceType() == 1) {
            postImageView.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
            drawBtn.setVisibility(View.VISIBLE);
            UtilImage.loadImageWithPicasso(this, postImageView, post.getSource());
        } else if (post.getSourceType() == 2) {
            videoSource = post.getSource().split("=")[1];
            setVideo();
            drawBtn.setVisibility(View.GONE);
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
                viewHolder.userImageView.setImageBitmap(UtilImage.getBitmap(context, val.getUserPicture()));
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
                UtilImage.loadImageWithPicasso(context, viewHolder.madeImageView, val.getMadeSource());


            }

            @Override
            public void onItemClick(View view) {

            }
        };
        madeRecyclerView.setAdapter(madeGenericAdapter);

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
        private CircularImageView userImageView;
        private TextView userText, timeText, commentText;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userImageView = (CircularImageView) itemView.findViewById(R.id.userImageView);
            userText = (TextView) itemView.findViewById(R.id.userText);
            timeText = (TextView) itemView.findViewById(R.id.timeText);
            commentText = (TextView) itemView.findViewById(R.id.commentText);
        }
    }


    private class MadeViewHolder extends RecyclerView.ViewHolder {
        private ImageView madeImageView;


        public MadeViewHolder(View itemView) {
            super(itemView);
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
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION |
                YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        if (!wasRestored) {
            youTubePlayer.cueVideo(videoSource);
        }
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


}
