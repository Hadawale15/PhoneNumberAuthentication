package com.example.otp_authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ManageOTP extends AppCompatActivity {
    EditText enterOtp;
    Button next;
    String phoneNo,otpid;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_otp);

        enterOtp=findViewById(R.id.emter_otp_id);
        next=findViewById(R.id.next_id);

        mAuth=FirebaseAuth.getInstance();

        phoneNo=getIntent().getStringExtra("mobile").toString();

        initiateOTP();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterOtp.getText().toString().isEmpty())
                {
                    Toast.makeText(ManageOTP.this, "can't Empty", Toast.LENGTH_SHORT).show();
                }
                else  if (enterOtp.getText().toString().length()!=6){
                    Toast.makeText(ManageOTP.this, "invald", Toast.LENGTH_SHORT).show();
                }
                else {
                    PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(otpid,enterOtp.getText().toString());
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });
    }

    private void initiateOTP() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid=s;
                                Toast.makeText(ManageOTP.this, "Code sent Successfully"+otpid, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ManageOTP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent=new Intent(ManageOTP.this,DashBoard.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(ManageOTP.this, Exception.class.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}