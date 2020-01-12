package cz.vasic2000.icash.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.vasic2000.icash.R;
import cz.vasic2000.icash.mvp.model.image.IImageLoader;
import cz.vasic2000.icash.mvp.presenter.MainPresenter;
import cz.vasic2000.icash.mvp.view.MainView;
import cz.vasic2000.icash.ui.adapter.RepositoriesRVAdapter;
import cz.vasic2000.icash.ui.image.GlideImageLoader;
import io.reactivex.android.schedulers.AndroidSchedulers;
import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;


public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.tv_username)
    TextView usernameTextView;

    @BindView(R.id.iv_avatar)
    ImageView avatarImageView;

    @BindView(R.id.rl_loading)
    RelativeLayout loadingRelativeLayout;

    RepositoriesRVAdapter adapter;

    IImageLoader<ImageView> imageLoader = new GlideImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @ProvidePresenter
    public MainPresenter providePresenter(){
        return new MainPresenter(AndroidSchedulers.mainThread());
    }

    @Override
    public void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RepositoriesRVAdapter(presenter.getCountriesListPresenter());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        loadingRelativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void setUsername(String login) {
        usernameTextView.setText(login);
    }

    @Override
    public void loadImage(String avatarUrl) {
        imageLoader.loadInto(avatarUrl, avatarImageView);
    }
}


