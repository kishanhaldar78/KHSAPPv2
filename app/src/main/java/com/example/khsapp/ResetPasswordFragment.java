package com.example.khsapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    private EditText registeredEmail;
    private Button resetPasswordBtn;
    private TextView goBack;

    private FrameLayout parentFrameLayout;

    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        registeredEmail=view.findViewById(R.id.forgot_password_email);
        resetPasswordBtn=view.findViewById(R.id.reset_password_btn);
        goBack=view.findViewById(R.id.tv_forgot_password_go_back);

        parentFrameLayout=getActivity().findViewById(R.id.register_flamelayout);

        emailIconContainer=view.findViewById(R.id.forgot_password_email_icon_container);
        emailIcon=view.findViewById(R.id.forgot_password_email_icon);
        emailIconText=view.findViewById(R.id.forgot_password_email_icon_text);
        progressBar=view.findViewById(R.id.forgot_password_progressbar);

        firebaseAuth=FirebaseAuth.getInstance();

        return  view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registeredEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIconText.setVisibility(View.GONE);

                //enable email icon with transition
                TransitionManager.beginDelayedTransition(emailIconContainer); //Linear layout
                emailIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                //disable button so user not connect again & again

                resetPasswordBtn.setEnabled(false);
                resetPasswordBtn.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()) {

                                   ScaleAnimation scaleAnimation;
                                   scaleAnimation = new ScaleAnimation(1,0,1,0,emailIcon.getWidth()/2,emailIcon.getHeight()/2);
                                   scaleAnimation.setDuration(100);
                                   scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                   scaleAnimation.setRepeatMode(Animation.REVERSE);
                                   scaleAnimation.setRepeatCount(1);
                                   scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                       @Override
                                       public void onAnimationStart(Animation animation) {

                                       }

                                       @Override
                                       public void onAnimationEnd(Animation animation) {
                                           TransitionManager.beginDelayedTransition(emailIconContainer);
                                           emailIconText.setText("Recovery email sent successfully ! check your inbox");
                                           emailIconText.setTextColor(getResources().getColor(R.color.successGreen));
                                           emailIconText.setTextColor(getResources().getColor(R.color.successGreen));
                                           emailIcon.setVisibility(View.VISIBLE);
                                           emailIconText.setVisibility(View.VISIBLE);
                                       }

                                       @Override
                                       public void onAnimationRepeat(Animation animation) {
                                        emailIcon.setImageResource(R.drawable.ic_email_green_24dp);
                                       }
                                   });

                                   emailIcon.startAnimation(scaleAnimation);
                                   // Toast.makeText(getActivity(),"Email send sucessfully",Toast.LENGTH_LONG).show();
                               }else{
                                   String error = task.getException().getMessage();
                                   //Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                   //Here we set Red color mail id for error purpose with transition animation with linearlayout
                                   TransitionManager.beginDelayedTransition(emailIconContainer);
                                   emailIconText.setText(error);
                                   emailIconText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                   emailIconText.setVisibility(View.VISIBLE);
                                   resetPasswordBtn.setEnabled(true);
                                   resetPasswordBtn.setTextColor(Color.rgb(255,255,255));

                               }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });

    }

    private void checkInputs() {
        if(TextUtils.isEmpty(registeredEmail.getText())){
            resetPasswordBtn.setEnabled(false);
            resetPasswordBtn.setTextColor(Color.argb(50,255,255,255));
        }else{
            resetPasswordBtn.setEnabled(true);
            resetPasswordBtn.setTextColor(Color.rgb(255,255,255));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();

    }

}
