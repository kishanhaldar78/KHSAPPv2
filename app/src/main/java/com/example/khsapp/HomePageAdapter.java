package com.example.khsapp;

import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomepPageModel> homepPageModelList;

    public HomePageAdapter(List<HomepPageModel> homepPageModelList) {
        this.homepPageModelList = homepPageModelList;
    }


    //It return Item ViewType
    @Override
    public int getItemViewType(int position) {

        switch (homepPageModelList.get(position).getType()) {
            case 0:
                return HomepPageModel.BANNER_SLIDER;
            case 1:
                return HomepPageModel.STRIP_AD_BANNER;
            case 2:
                return HomepPageModel.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return HomepPageModel.GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case HomepPageModel.BANNER_SLIDER:
                View bannerSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout, parent, false);
                return new BannerSliderViewHolder(bannerSliderView);
            case HomepPageModel.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new StripAdBannerViewHolder(stripAdView);
            case HomepPageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizontalProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductViewholder(horizontalProductView);
            case HomepPageModel.GRID_PRODUCT_VIEW:
                View gridProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);
                return new GridProductViewViewholder(gridProductView);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Here we doing dada binding
        switch (homepPageModelList.get(position).getType()) {
            case HomepPageModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList = homepPageModelList.get(position).getSliderModelList();
                //Here type of view so we need to type cast
                ((BannerSliderViewHolder) holder).setBannerSliderViwePager(sliderModelList);
                break;
            case HomepPageModel.STRIP_AD_BANNER:
                int resource = homepPageModelList.get(position).getResource();
                String color = homepPageModelList.get(position).getBackgroundColor();
                ((StripAdBannerViewHolder) holder).setStripAd(resource, color);
                break;
            case HomepPageModel.HORIZONTAL_PRODUCT_VIEW:
                String horizontalLayoutTitle=homepPageModelList.get(position).getTitle();
                List<HorizontalProductScrollModel> horizontalProductScrollModelList=homepPageModelList.get(position).getHorizontalProductScrollModelList();
                ((HorizontalProductViewholder)holder).setHorizontalProductLayout(horizontalProductScrollModelList,horizontalLayoutTitle);
                break;
            case HomepPageModel.GRID_PRODUCT_VIEW:
                String gridLayoutTitle=homepPageModelList.get(position).getTitle();
                List<HorizontalProductScrollModel> gridProductScrollModelList=homepPageModelList.get(position).getHorizontalProductScrollModelList();
                ((GridProductViewViewholder)holder).setGridProductLayout(gridProductScrollModelList,gridLayoutTitle);
                break;

            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return homepPageModelList.size();
    }

    /////////// Banner Slider start
    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {


        private ViewPager2 bannerSliderViwePager;
        private int currentPage = 2;  // set 2 bcz  our banner original at index no. 2
        private Timer timer;
        final private long DELEY_TIME = 3000; //these timer variable
        final private long PERIOD_TIME = 3000;
        private Handler sliderHandler = new Handler();

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViwePager = itemView.findViewById(R.id.banner_slider_view_pager);

        }

        private void setBannerSliderViwePager(final List<SliderModel> sliderModelList) {
            SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList, bannerSliderViwePager);

            //need to set Adpter with pageView
            bannerSliderViwePager.setAdapter(new SliderAdapter(sliderModelList, bannerSliderViwePager));
            bannerSliderViwePager.setAdapter(sliderAdapter);
            bannerSliderViwePager.setClipToPadding(false);
            bannerSliderViwePager.setClipChildren(false);
            bannerSliderViwePager.setOffscreenPageLimit(3);
            bannerSliderViwePager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            // bannerSliderViwePager.setCurrentItem(currentPage);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
                }
            });
            bannerSliderViwePager.setPageTransformer(compositePageTransformer);

            bannerSliderViwePager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) { //using the event we know which user perform action
                    pageLooper(sliderModelList);
                    stopBannerSlideShow();

                    if (event.getAction() == MotionEvent.ACTION_UP) {//it means user remover his finger and action_down user touch
                        startBannerSlideShow(sliderModelList);
                    }
                    return false;
                }
            });

            bannerSliderViwePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    sliderHandler.removeCallbacks(sliderRunnable);
                    sliderHandler.postDelayed(sliderRunnable, 3000);//Slide duration 3 Seconds

                }
            });
        }

        private Runnable sliderRunnable = new Runnable() {
            @Override
            public void run() {
                bannerSliderViwePager.setCurrentItem(bannerSliderViwePager.getCurrentItem() + 1);
            }
        };

        private void pageLooper(List<SliderModel> sliderModelList) {

            if (currentPage == (sliderModelList.size() - 2)) {
                currentPage = 2;
                bannerSliderViwePager.setCurrentItem(currentPage, false);  //false animation bcz user does not know banner shifted

            }

            if (currentPage == 1) {
                currentPage = sliderModelList.size() - 3;
                bannerSliderViwePager.setCurrentItem(currentPage, false);  //false animation bcz user does not know banner shifted

            }


        }

        private void startBannerSlideShow(final List<SliderModel> sliderModelList) {
            //we use timer to animate
            final Handler handler = new Handler();
            final Runnable update = new Runnable() { //thread
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size()) {
                        currentPage = 1;
                    }
                    bannerSliderViwePager.setCurrentItem(currentPage++, true);//here animation is true
                }
            };

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELEY_TIME, PERIOD_TIME);

        }

        private void stopBannerSlideShow() {
            timer.cancel();
            ;
        }


    }

    ///////// Banner Slider end

    ///////// Strip Ad Start

    public class StripAdBannerViewHolder extends RecyclerView.ViewHolder {

        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        public StripAdBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_add_container);

        }

        private void setStripAd(int resource, String color) {
            stripAdImage.setImageResource(resource);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }

    }

    ///////// Strip Ad End

    //////Horizontal Product Layout && GRID Product start

    public class HorizontalProductViewholder extends RecyclerView.ViewHolder{

        private TextView horizontalLayoutTitle;
        private Button horizontalViewAllBtn;
        private RecyclerView horizaontalRecyclerView;

        public HorizontalProductViewholder(@NonNull View itemView) {
            super(itemView);
            horizontalLayoutTitle=itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalViewAllBtn=itemView.findViewById(R.id.horizontal_scroll_view_all_button);
            horizaontalRecyclerView=itemView.findViewById(R.id.horizontal_scroll_product_recyclerView);

        }

        private void setHorizontalProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList,String title){

            horizontalLayoutTitle.setText(title);

            //If more than one more product then we visiable viewALL button

            if(horizontalProductScrollModelList.size()>8){
                horizontalViewAllBtn.setVisibility(View.VISIBLE);
            }else{
                horizontalViewAllBtn.setVisibility(View.INVISIBLE);
            }

            HorizontalProductScrollAdapter horizontalProductScrollAdapter=new HorizontalProductScrollAdapter(horizontalProductScrollModelList);

            //Here we set linear layout
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizaontalRecyclerView.setLayoutManager(linearLayoutManager);

            //now we set adapter
            horizaontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }
    }

    //////Horizontal Product Layout && Grid Product END

    ////// Grid Product Layout start

    public class  GridProductViewViewholder extends  RecyclerView.ViewHolder{

        TextView gridLayoutTitle;
        Button gridLayoutAllBtn;
        GridView gridView;

        public GridProductViewViewholder(@NonNull View itemView) {
            super(itemView);
             gridLayoutTitle=itemView.findViewById(R.id.grid_product_layout_title);
             gridLayoutAllBtn= itemView.findViewById(R.id.grid_product_layout_viewall_btn);
             gridView= itemView.findViewById(R.id.grid_product_layout_gridview);
        }

        private void setGridProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList,String title){
            gridLayoutTitle.setText(title);
            gridView.setAdapter(new GridProductLayoutAdapter(horizontalProductScrollModelList));
        }
    }

    ////// Grid Product Layout end
}
