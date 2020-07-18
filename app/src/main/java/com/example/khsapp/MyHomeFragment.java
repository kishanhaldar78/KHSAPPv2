package com.example.khsapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyHomeFragment extends Fragment {


    private RecyclerView categoryRecycleView;
    private CategoryAdapter categoryAdapter;

    ///////// Banner Slider

    private ViewPager2 bannerSliderViwePager;
    private List<SliderModel> sliderModelList;
    private int currentPage=2;  // set 2 bcz  our banner original at index no. 2
    private Timer timer;
    final private long DELEY_TIME=3000; //these timer variable
    final private long PERIOD_TIME=3000;
    private Handler sliderHandler=new Handler();

    ///////////Banner Slider


    ///////// Strip Ad

    private ImageView stripAdImage;
    private ConstraintLayout stripAdContainer;
    ///////// Strip Ad


    //////Horizontal Product Layout

    private TextView horizontalLayoutTitle;
    private Button horizontalViewAllBtn;
    private RecyclerView horizaontalRecyclerView;

    //////Horizontal Product Layout


    public MyHomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_my_home, container, false);

        categoryRecycleView=view.findViewById(R.id.category_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecycleView.setLayoutManager(layoutManager);

        //tempory list

        List<CategoryModel>categoryModelList=new ArrayList<CategoryModel>();
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Electronic"));
        categoryModelList.add(new CategoryModel("link","Applicance"));
        categoryModelList.add(new CategoryModel("link","Furniture"));
        categoryModelList.add(new CategoryModel("link","Fashion"));
        categoryModelList.add(new CategoryModel("link","Toys"));
        categoryModelList.add(new CategoryModel("link","Sports"));
        categoryModelList.add(new CategoryModel("link","Wall art"));
        categoryModelList.add(new CategoryModel("link","Books"));
        categoryModelList.add(new CategoryModel("link","Shoes"));

        //attach the lsit in Adapter
        categoryAdapter = new CategoryAdapter(categoryModelList);
        //set this adapter in RecycleView
        categoryRecycleView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        ///////// Banner Slider

        bannerSliderViwePager=view.findViewById(R.id.banner_slider_view_pager);

        sliderModelList=new ArrayList<SliderModel>();
        //make the slider to infinity

        sliderModelList.add(new SliderModel(R.drawable.ic_report_problem_black_24dp,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.forgot_password,"#077AE4"));

        sliderModelList.add(new SliderModel(R.drawable.ic_email_red_24dp,"#077AE4"));//it is index no.2
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_local_mall_black_24,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_email_green_24dp,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_share_black_24,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_shopping_cart_black_24,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_contact_support_black_24,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_report_problem_black_24dp,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.forgot_password,"#077AE4")); //last banner

        sliderModelList.add(new SliderModel(R.drawable.ic_email_red_24dp,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_local_mall_black_24,"#077AE4"));

        SliderAdapter sliderAdapter=new SliderAdapter(sliderModelList,bannerSliderViwePager);

        //need to set Adpter with pageView
        bannerSliderViwePager.setAdapter(new SliderAdapter(sliderModelList,bannerSliderViwePager));
        bannerSliderViwePager.setAdapter(sliderAdapter);
        bannerSliderViwePager.setClipToPadding(false);
        bannerSliderViwePager.setClipChildren(false);
        bannerSliderViwePager.setOffscreenPageLimit(3);
        bannerSliderViwePager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

       // bannerSliderViwePager.setCurrentItem(currentPage);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        bannerSliderViwePager.setPageTransformer(compositePageTransformer);

        bannerSliderViwePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);//Slide duration 3 Seconds

            }
        });

        //if user touch then cancel slideshow
/*
        bannerSliderViwePager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { //using the event we know which user perform action
                pageLooper();
                stopBannerSlideShow();

                if(event.getAction()==MotionEvent.ACTION_UP){//it means user remover his finger and action_down user touch
                    startBannerSlideShow();
                }
                return false;
            }
        });

        //if user remove hand then again start slide show
*/
        ///////////Banner Slider

        ///////// Strip Ad

          stripAdImage=view.findViewById(R.id.strip_ad_image);
          stripAdContainer=view.findViewById(R.id.strip_add_container);

          stripAdImage.setImageResource(R.drawable.stripadd);
          stripAdContainer.setBackgroundColor(Color.parseColor("#FF4038"));
        ///////// Strip Ad


        //////Horizontal Product Layout

        horizontalLayoutTitle=view.findViewById(R.id.horizontal_scroll_layout_title);
        horizontalViewAllBtn=view.findViewById(R.id.horizontal_scroll_view_all_button);
        horizaontalRecyclerView=view.findViewById(R.id.horizontal_scroll_product_recyclerView);

        List<HorizontalProductScrollModel> horizontalProductScrollModelList=new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"Redmi 5A","SD 625 Processor","RS.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"Redmi 6A","SD 667 Processor","RS.6999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"Redmi note 5 pro","SD 720 Processor","RS.10099/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"Nokia 3210","Media Teck","RS.7999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"Realme pro 3","SD 845 Processor","RS.12999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"Realme 2 pro","SD 435 Processor","RS.8999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"LG 5A","SD 112 Processor","RS.2999/-"));
       // horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"Motorola 3A","34 Processor","RS.9999/-"));
       // horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"Micromax 5A","612 Processor","RS.5999/-"));
        //horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"Samsung M31","9611 Processor","RS.16999/-"));
        //horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_baseline_mobile_friendly_black_24,"Samusang M21","9611 Processor","RS.13999/-"));

        HorizontalProductScrollAdapter horizontalProductScrollAdapter=new HorizontalProductScrollAdapter(horizontalProductScrollModelList);

        //Here we set linear layout
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizaontalRecyclerView.setLayoutManager(linearLayoutManager);

        //now we set adapter
        horizaontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
        horizontalProductScrollAdapter.notifyDataSetChanged();


        //////Horizontal Product Layout

        ////// Grid Product Layout

        TextView gridLayoutTitle=view.findViewById(R.id.grid_product_layout_title);
        Button gridLayoutAllBtn= view.findViewById(R.id.grid_product_layout_viewall_btn);
        GridView gridView= view.findViewById(R.id.grid_product_layout_gridview);

        gridView.setAdapter(new GridProductLayoutAdapter(horizontalProductScrollModelList));

        ////// Grid Product Layout

        ///////////////////Recycle Test (Multi recycle view)

        RecyclerView testing = view.findViewById(R.id.testing);
        LinearLayoutManager testingLayoutManager=new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        testing.setLayoutManager(testingLayoutManager);

        List<HomepPageModel> homepPageModelList=new ArrayList<>();
        homepPageModelList.add(new HomepPageModel(0,sliderModelList));
        homepPageModelList.add(new HomepPageModel(1,R.drawable.stripadd,"#000000"));
        homepPageModelList.add(new HomepPageModel(2,"Deals Of the Day",horizontalProductScrollModelList));
        homepPageModelList.add(new HomepPageModel(3,"Deals Of the Day",horizontalProductScrollModelList));
        homepPageModelList.add(new HomepPageModel(1,R.drawable.stripadd,"#ffff00"));
        homepPageModelList.add(new HomepPageModel(3,"Deals Of the Day",horizontalProductScrollModelList));
        homepPageModelList.add(new HomepPageModel(2,"Deals Of the Day",horizontalProductScrollModelList));
        homepPageModelList.add(new HomepPageModel(1,R.drawable.banner,"#ffff00"));
        homepPageModelList.add(new HomepPageModel(0,sliderModelList));

        HomePageAdapter adapter=new HomePageAdapter(homepPageModelList);
        testing.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ///////////////////Recycle Test End

        return  view;
    }

    ///////Banner Slider
    //make looper to make our banner slider to infinite
  /*  private  void pageLooper() {

        if(currentPage == (sliderModelList.size() - 2)){
            currentPage=2;
            bannerSliderViwePager.setCurrentItem(currentPage,false);  //false animation bcz user does not know banner shifted

        }

        if(currentPage ==1){
              currentPage=sliderModelList.size()-3;
            bannerSliderViwePager.setCurrentItem(currentPage,false);  //false animation bcz user does not know banner shifted

        }


    }


    private  void startBannerSlideShow(){
        //we use timer to animate
        final Handler handler=new Handler();
        final Runnable update=new Runnable() { //thread
            @Override
            public void run() {
                if (currentPage >= sliderModelList.size()) {
                    currentPage=1;
                }
                    bannerSliderViwePager.setCurrentItem(currentPage++, true);//here animation is true
            }
        };

        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
              handler.post(update);
            }
        },DELEY_TIME,PERIOD_TIME);

    }

    private void stopBannerSlideShow(){
        timer.cancel();;
    }
*/
    ///////Banner Slider


    private  Runnable sliderRunnable=new Runnable() {
        @Override
        public void run() {
            bannerSliderViwePager.setCurrentItem(bannerSliderViwePager.getCurrentItem()+1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
        sliderHandler.postDelayed(sliderRunnable,3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,3000);
    }

}