package cz.vasic2000.icash.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.vasic2000.icash.R;
import cz.vasic2000.icash.mvp.presenter.list.IRepositoryListPresenter;
import cz.vasic2000.icash.mvp.view.list.ReposotoryItemView;

import com.jakewharton.rxbinding2.view.RxView;


public class RepositoriesRVAdapter extends RecyclerView.Adapter<RepositoriesRVAdapter.ViewHolder> {
    private IRepositoryListPresenter presenter;

    public RepositoriesRVAdapter(IRepositoryListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repository, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.pos = position;
        presenter.bind(holder);
        RxView.clicks(holder.itemView).map(o -> holder).subscribe(presenter.getClickSubject());
    }

    @Override
    public int getItemCount() {
        return presenter.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ReposotoryItemView {
        int pos = 0;

        @BindView(R.id.tv_title)
        TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public int getPos() {
            return pos;
        }

        @Override
        public void setTitle(String title) {
            titleTextView.setText(title);
        }
    }
}
