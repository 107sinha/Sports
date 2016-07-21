package com.example.deepanshu.sportscafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.deepanshu.sportscafe.R;
import com.example.deepanshu.sportscafe.SharedPreference;
import com.example.deepanshu.sportscafe.Utility;
import com.example.deepanshu.sportscafe.model.Students;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class AddStudent extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PICTURE = 100;
    private static final int REQUEST_CAMERA = 1;

    String userChoosenTask;

    private static final String TAG = AddStudent.class.getSimpleName();

    private TextInputLayout inputname;
    private TextInputLayout inputaddr;
    private TextInputLayout inputemail;
    private TextInputLayout inputphone;

    private EditText name;
    private EditText addr;
    private EditText email;
    private EditText phone;

    private ImageView profile;

    ProgressBar progressBar;

    private SharedPreference shared;

    private String studName, studAddr, studEmail, studPhone, studImage;

    private RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            // todo log exception

            // important to return false so the error placeholder can be placed
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        inputname = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputaddr = (TextInputLayout) findViewById(R.id.input_layout_addr);
        inputemail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputphone = (TextInputLayout) findViewById(R.id.input_layout_phone);

        name = (EditText) findViewById(R.id.input_name);
        addr = (EditText) findViewById(R.id.input_addr);
        email = (EditText) findViewById(R.id.input_email);
        phone = (EditText) findViewById(R.id.input_phone);

        name.addTextChangedListener(new MyTextWatcher(inputname));
        addr.addTextChangedListener(new MyTextWatcher(inputaddr));
        email.addTextChangedListener(new MyTextWatcher(inputemail));
        phone.addTextChangedListener(new MyTextWatcher(inputphone));

        profile = (ImageView) findViewById(R.id.profile);
        profile.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        shared = new SharedPreference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_done:
                submitForm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Choose an image from Gallery */
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == SELECT_PICTURE) {
//                // Get the url from data
//                Uri selectedImageUri = data.getData();
//                if (null != selectedImageUri) {
//                    // Get the path from the Uri
//                    studImage = getPathFromURI(selectedImageUri);
//                    Log.i(TAG, "Image Path : " + studImage);
//                    // Set the image in ImageView
//                    Glide.with(this)
//                            .load(studImage)
//                            .centerCrop()
//                            .crossFade()
//                            .thumbnail(0.5f)
//                            .transform(new CircleTransform(this))
//                            .listener(requestListener)
//                            .placeholder(R.drawable.profile_placeholder)
//                            .error(R.drawable.profile_placeholder)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(profile);
//                }
//            }
//        }
//    }

        /* Get the real path from the URI */

    private String getPathFromURI(Uri contentUri) {
            String res = null;
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            if (cursor != null) {
                cursor.close();
            }
            return res;
        }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateAddr()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }

        Students students = new Students();
        students.setImage(studImage);
        students.setName(studName);
        students.setAddress(studAddr);
        students.setEmail(studEmail);
        students.setNumber(studPhone);;

        shared.addFavorite(this, students);
        Toast.makeText(getApplicationContext(), "Successfull Added", Toast.LENGTH_SHORT).show();
    }

    private boolean validateName() {
        studName = name.getText().toString().trim();

        if (studName.isEmpty() || isValidName(studName)) {
            inputname.setError(getString(R.string.err_msg_name));
            requestFocus(name);
            return false;
        } else {
            inputname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAddr() {
        studAddr = addr.getText().toString().trim();

        if (studAddr.isEmpty() || isValidAddr(studAddr)) {
            inputaddr.setError(getString(R.string.err_msg_addr));
            requestFocus(addr);
            return false;
        } else {
            inputaddr.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        studEmail = email.getText().toString().trim();

        if (studEmail.isEmpty() || !isValidEmail(studEmail)) {
            inputemail.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            inputemail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        studPhone = phone.getText().toString().trim();

        if (studPhone.isEmpty() || !isValidPhone(studPhone)) {
            inputphone.setError(getString(R.string.err_msg_phone));
            requestFocus(phone);
            return false;
        } else {
            inputphone.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fab:
//                openImageChooser();
                selectImage();
                break;

            case R.id.profile:
//                openImageChooser();
                selectImage();
                break;
        }
    }

    private static boolean isValidName(String name) {
        return !TextUtils.isEmpty(name) && Pattern.compile("[A-Z][a-zA-Z]*").matcher(name).matches();
    }

    private static boolean isValidAddr(String addr) {
        return !TextUtils.isEmpty(addr) && Pattern.compile("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)").matcher(addr).matches();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) && Pattern.compile("[1-9]\\d{2}[1-9]\\d{2}\\d{4}").matcher(phone).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_addr:
                    validateAddr();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_phone:
                    validatePhone();
                    break;
            }
        }
    }

    public static class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddStudent.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddStudent.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(AddStudent.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_PICTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
//code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    studImage = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + studImage);
                    // Set the image in ImageView
                    Glide.with(this)
                            .load(studImage)
                            .centerCrop()
                            .crossFade()
                            .thumbnail(0.5f)
                            .transform(new CircleTransform(this))
                            .listener(requestListener)
                            .placeholder(R.drawable.profile_placeholder)
                            .error(R.drawable.profile_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profile);
                }
//        Bitmap bm=null;
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        studImage = BitMapToString(bm);
//        Toast.makeText(getApplicationContext(), studImage, Toast.LENGTH_LONG).show();
//        profile.setImageBitmap(bm);
//        Glide.with(this)
//                .load(BitMapToString(bm))
//                .centerCrop()
//                .crossFade()
//                .thumbnail(0.5f)
//                .transform(new CircleTransform(this))
//                .placeholder(R.drawable.profile_placeholder)
//                .error(R.drawable.profile_placeholder)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(profile);

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri uri = Uri.fromFile(destination);
        studImage = uri.toString();
        profile.setImageBitmap(thumbnail);

        Toast.makeText(getApplicationContext(), studImage, Toast.LENGTH_LONG).show();

    }

}
