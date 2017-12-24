package com.shaheryarbhatti.polaroidapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

public class ItemSelelectedActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    private final String TAG = "ItemSelelectedActivity";
    private Toolbar toolbar;
    private TextView descriptionText;
    private ImageView postImageView;
    private RecyclerView commentRecyclerView, madeRecyclerView;

    private ArrayList<Comment> commentList;
    private ArrayList<Made> madeList;
    private GenericAdapter<Comment> commentGenericAdapter;
    private GenericAdapter<Made> madeGenericAdapter;
    private LinearLayoutManager commentLayoutManager;
    private GridLayoutManager madeLayoutManager;
    private View commentSectionView, madeSectionView;
    private TextView commentText, madeText;
    private FrameLayout videoLayout;
    private Button letsDrawButton;
    private ScrollView scroll;


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
        prepareViews();
       /*
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#606060")));
        getSupportActionBar().setElevation(0);*/


    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        descriptionText = (TextView) findViewById(R.id.descriptionText);
        postImageView = (ImageView) findViewById(R.id.postImageView);
        commentSectionView = findViewById(R.id.commentSectionView);
        madeSectionView = findViewById(R.id.madeSectionView);
        commentText = (TextView) commentSectionView.findViewById(R.id.sectionText);
        madeText = (TextView) madeSectionView.findViewById(R.id.sectionText);
        commentRecyclerView = (RecyclerView) commentSectionView.findViewById(R.id.sectoinsRecyclerView);
        madeRecyclerView = (RecyclerView) madeSectionView.findViewById(R.id.sectoinsRecyclerView);
        videoLayout = (FrameLayout) findViewById(R.id.frame_fragment);
        letsDrawButton = (Button) findViewById(R.id.drawBtn);
        ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
        scroll.setFocusableInTouchMode(true);
        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    }

    private void prepareViews() {
        commentText.setText("Comment");
        madeText.setText("Made");
        Bundle data = getIntent().getBundleExtra("post");
        Post post = (Post) data.getSerializable("post");
        descriptionText.setText(post.getTitleText());
        commentList = post.getComments();
        madeList = post.getMade();
        prepareCommentRecylerView();
        if (post.getSourceType() == 1) {
            postImageView.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
            letsDrawButton.setVisibility(View.VISIBLE);
            UtilImage.loadImageWithPicasso(this, postImageView, post.getSource());
        } else if (post.getSourceType() == 2) {
            setVideo();
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
                viewHolder.userTypeText.setText(val.getProfesson());
                viewHolder.commentText.setText(val.getCommentText());
                viewHolder.timeText.setText(val.getCommentDuration());


            }

            @Override
            public void onItemClick(View view) {

            }

        };
        commentRecyclerView.setAdapter(commentGenericAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView;
        private TextView userText, userTypeText, timeText, commentText;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userImageView = (ImageView) itemView.findViewById(R.id.userImageView);
            userText = (TextView) itemView.findViewById(R.id.userText);
            userTypeText = (TextView) itemView.findViewById(R.id.userTypeText);
            timeText = (TextView) itemView.findViewById(R.id.timeText);
            commentText = (TextView) itemView.findViewById(R.id.commentText);
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
            youTubePlayer.cueVideo("-OQ0QwgHYDo");
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
