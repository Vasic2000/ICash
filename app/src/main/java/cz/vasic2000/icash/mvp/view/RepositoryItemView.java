package cz.vasic2000.icash.mvp.view;

import androidx.annotation.NonNull;

public interface RepositoryItemView {
    int getPos();
    void setTitle(@NonNull String title);
}
