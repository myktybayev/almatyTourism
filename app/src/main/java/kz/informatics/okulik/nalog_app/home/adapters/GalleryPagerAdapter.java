package kz.informatics.okulik.nalog_app.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kz.informatics.okulik.R;

public class GalleryPagerAdapter extends RecyclerView.Adapter<GalleryPagerAdapter.GalleryViewHolder> {

    private final int[] galleryPhotos;

    public GalleryPagerAdapter(int[] galleryPhotos) {
        this.galleryPhotos = galleryPhotos != null ? galleryPhotos : new int[0];
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_preview, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        if (position >= 0 && position < galleryPhotos.length) {
            holder.imageView.setImageResource(galleryPhotos[position]);
        }
    }

    @Override
    public int getItemCount() {
        return galleryPhotos.length;
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageGalleryPreview);
        }
    }
}
