package Adapters;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.wmeaapp.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> imageUrls;
    private Timer timer;
    private Handler handler;
    private Runnable runnable;
    private ViewPager viewPager;

    public ImagePagerAdapter(Context context, List<String> imageUrls, ViewPager viewPager) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.viewPager = viewPager;
        handler = new Handler(Looper.getMainLooper());
        startAutoSlide();
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);

        // Load image from URL using Glide
        Glide.with(context)
                .load(imageUrls.get(position))
                .centerCrop()
                .placeholder(R.drawable.camera_icon) // Placeholder image while loading
                .error(R.drawable.chatbot) // Image to display if loading fails
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
    public void startAutoSlide() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 3000, 3000);

        runnable = new Runnable() {
            @Override
            public void run() {
                int nextPosition = (viewPager.getCurrentItem() + 1) % imageUrls.size();
                viewPager.setCurrentItem(nextPosition, true);
            }
        };
    }

    public void stopAutoSlide() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
