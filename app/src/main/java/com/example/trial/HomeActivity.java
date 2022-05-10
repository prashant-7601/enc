package com.example.trial;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.NoSuchPaddingException;

public class HomeActivity extends AppCompatActivity {

    Button choose, encrypt, decrypt, generate;
    EditText key, name;
    TextView show_path;
    ImageView preview;
    Intent myFileIntent;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String username = currentUser.getEmail();
    Uri uri;
    String sImage, my_key = "1tVkg0knCiDc9k80", my_spec_key = "BentH1diPooEawVa";
    File myDir;
    String encrypted_file_name = "encrypted.jpeg", decrypted_file_name = "decrypted.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        show_path = findViewById(R.id.path);
        encrypt = findViewById(R.id.encrypt);
        decrypt = findViewById(R.id.decrypt);
        BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setSelectedItemId(R.id.home);
        preview = findViewById(R.id.preview);
        preview.setImageBitmap(null);
        key = findViewById(R.id.key);
        name = findViewById(R.id.name);
        generate = findViewById(R.id.generate);
        myDir = new File(Environment.getExternalStorageDirectory().toString() + "/Saved_Images");
        if(!myDir.exists())
            myDir.mkdir();
        Log.d("testing", username);
        Dexter.withActivity(this).withPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                //Toast.makeText(HomeActivity.this, "Permissions granted!!", Toast.LENGTH_LONG).show();
                Log.d("myTag", report.toString());
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Toast.makeText(HomeActivity.this, "You must enable permissions!", Toast.LENGTH_LONG).show();
            }
        }).check();

        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.logs:
                        startActivity(new Intent(getApplicationContext(), LogsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Home:
                        return true;
                }
                return false;
            }
        });
        ActivityResultLauncher<Intent> launchFileActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            String path = data.getData().getPath();
                            show_path.setText(path);
                            preview.setImageURI(uri);
                        }
                    }
                });
        choose = findViewById(R.id.select);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launchFileActivity.launch(myFileIntent);
            }
        });

        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show_path.getText().toString().equals("None")){
                    show_path.setError("Select an image!");
                    return;
                }
                if(key.getText().toString().isEmpty()){
                    key.setError("Enter a key!");
                    return;
                }
                if(key.getText().toString().length()!=16){
                    key.setError("Length should be exactly 16!");
                    return;
                }
                if(name.getText().toString().isEmpty()){
                    name.setError("Enter new file name!");
                    return;
                }
                my_key = key.getText().toString();
                encrypted_file_name = name.getText().toString() + ".jpg";
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                InputStream is = new ByteArrayInputStream(stream.toByteArray());
                File outputFileEnc = new File(myDir, encrypted_file_name);
                try {
                    MyEncrypter.encryptToFile(my_key, my_spec_key, is, new FileOutputStream(outputFileEnc));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
                finally {
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    UserHelperClass user_logs = new UserHelperClass("encryption", my_key, encrypted_file_name, currentDate, currentTime);
                    UserHandler handler = new UserHandler(HomeActivity.this, "LOGS", null, 1);
                    handler.addInfo(user_logs);
                    handler.close();
                }
            }
        });

        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show_path.getText().toString().equals("None")){
                    show_path.setError("Select an image!");
                    return;
                }
                if(key.getText().toString().isEmpty()){
                    key.setError("Enter a key!");
                    return;
                }
                if(key.getText().toString().length()!=16){
                    key.setError("Length should be exactly 16!");
                    return;
                }
                if(name.getText().toString().isEmpty()){
                    name.setError("Enter new file name!");
                    return;
                }
                my_key = key.getText().toString();
                decrypted_file_name = name.getText().toString() + ".jpg";
                File outputFileDec  = new File(myDir, decrypted_file_name);
                try{
                    MyEncrypter.decryptToFile(my_key, my_spec_key, getContentResolver().openInputStream(uri), new FileOutputStream(outputFileDec));
                    preview.setImageURI(Uri.fromFile(outputFileDec));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
                finally {
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    UserHelperClass user_logs = new UserHelperClass("decryption", my_key, decrypted_file_name, currentDate, currentTime);
                    UserHandler handler = new UserHandler(HomeActivity.this, "LOGS", null, 1);
                    handler.addInfo(user_logs);
                    handler.close();
                }
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        + "0123456789"
                        + "abcdefghijklmnopqrstuvxyz";
                StringBuilder sb = new StringBuilder(16);
                for (int i = 0; i < 16; i++) {
                    int index
                            = (int)(AlphaNumericString.length()
                            * Math.random());
                    sb.append(AlphaNumericString
                            .charAt(index));
                }
                key.setText(sb.toString());
            }
        });
    }
}