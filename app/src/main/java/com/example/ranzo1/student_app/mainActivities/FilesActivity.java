package com.example.ranzo1.student_app.mainActivities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.objects.Subject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

import static com.google.android.gms.R.id.toolbar;
import static com.nightonke.boommenu.R.layout.bmb;


public class FilesActivity extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener{

    private static final String TAG = "Google Drive Activity";
    private static final int REQUEST_CODE_RESOLUTION = 1;
    private static final  int REQUEST_CODE_OPENER = 2;
    private GoogleApiClient mGoogleApiClient;
    private boolean fileOperation = false;
    private DriveId mFileId;
    public DriveFile file;
    private BoomMenuButton bmb;
    private int selectedIndex;
    DatabaseHendler db;
    ArrayList<Subject> subjects = new ArrayList<>();
    Metadata metaData;
    DriveId sFolderId;


    private static final int REQUEST_CODE_CREATOR = 3;

    private static final int REQUEST_CODE_STORE_FILE =4;
    private Bitmap mBitmapToSave;
    DriveId appfolderid;
    private List<String>namesSubject=new ArrayList<>();
    private Uri mFileUri;

    private RelativeLayout screen;
    MediaStore.Audio mp3;
    MediaRecorder mediaRecorder ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Files");



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }


        bmb=(BoomMenuButton)findViewById(R.id.bmb);
        didTapButton(bmb);


        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            if(i==0) {
                TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                        .normalImageRes(R.drawable.ic_eye_open)
                        .shadowEffect(true)
                        .imagePadding(new Rect(40, 40, 40, 40))
                        .normalTextColor(Color.BLACK)
                        .highlightedTextColor(Color.BLACK)
                        .normalColor(Color.parseColor("#a8c282"))
                        .highlightedColor(Color.WHITE)
                        .normalText("Open drive")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        onClickOpenFile();

                                    }
                                },500);

                            }
                        });
                bmb.addBuilder(builder);
            } else if(i==1) {
                TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                        .normalImageRes(R.drawable.ic_photo_1)
                        .shadowEffect(true)
                        .imagePadding(new Rect(50, 50, 50, 50))
                        .normalTextColor(Color.BLACK)
                        .highlightedTextColor(Color.BLACK)
                        .normalColor(Color.parseColor("#a8c282"))
                        .highlightedColor(Color.WHITE)
                        .normalText("Capture image and upload to drive")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                        startActivityForResult(intent, REQUEST_CODE_STORE_FILE);

                                    }
                                },500);




                            }
                        });
                bmb.addBuilder(builder);
            }  else if(i==2) {
                TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                        .normalImageRes(R.drawable.ic_photo)
                        .shadowEffect(true)
                        .imagePadding(new Rect(50, 50, 50, 50))
                        .normalTextColor(Color.BLACK)
                        .highlightedTextColor(Color.BLACK)
                        .normalColor(Color.parseColor("#a8c282"))
                        .highlightedColor(Color.WHITE)
                        .normalText("Upload image to drive")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                        galleryIntent.setType("image/jpeg");
                                        startActivityForResult(galleryIntent, REQUEST_CODE_STORE_FILE);
                                        fileOperation = false;

                                    }
                                },500);


                            }
                        });
                bmb.addBuilder(builder);
            }  else if(i==3) {
                TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                        .normalImageRes(R.drawable.ic_fileee)
                        .shadowEffect(true)
                        .imagePadding(new Rect(55, 55, 55, 55))
                        .normalTextColor(Color.BLACK)
                        .highlightedTextColor(Color.BLACK)
                        .normalColor(Color.parseColor("#a8c282"))
                        .highlightedColor(Color.WHITE)
                        .normalText("Upload file to drive")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("type */*");
                                        startActivityForResult(intent, REQUEST_CODE_STORE_FILE);

                                    }
                                },500);



                            }
                        });
                bmb.addBuilder(builder);
            } else if(i==4) {
                TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                        .normalImageRes(R.drawable.ic_technology)
                        .shadowEffect(true)
                        .imagePadding(new Rect(55, 55, 55, 55))
                        .normalTextColor(Color.BLACK)
                        .highlightedTextColor(Color.BLACK)
                        .normalColor(Color.parseColor("#a8c282"))
                        .highlightedColor(Color.WHITE)
                        .normalText("Upload audio file to drive")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {


                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        Thread recordInBackGround= new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);


                                                startActivityForResult(intent, REQUEST_CODE_STORE_FILE);
                                            }
                                        });

                                        recordInBackGround.start();

                                    }
                                },500);


                            }
                        });
                bmb.addBuilder(builder);
            } else if(i==5) {
                TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                        .normalImageRes(R.drawable.ic_folder_drive)
                        .shadowEffect(true)
                        .imagePadding(new Rect(50, 50, 50, 50))
                        .normalTextColor(Color.BLACK)
                        .highlightedTextColor(Color.BLACK)
                        .normalColor(Color.parseColor("#a8c282"))
                        .highlightedColor(Color.WHITE)
                        .normalText("Create folders of subjects on drive")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                createFolders();
                            }
                        });
                bmb.addBuilder(builder);
            }
        }




        screen = (RelativeLayout) findViewById(R.id.screen);

        db=DatabaseHendler.getInstance(this);

        subjects = (ArrayList<Subject>) db.getAllSubjects();



    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toasty.warning(this, "You denied. ", Toast.LENGTH_SHORT).show();
            }
        }
    }


////function for memType///////////
    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void saveFileToDrive(){



        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback
                (new ResultCallback<DriveContentsResult>() {
            @Override
            public void onResult(@NonNull final DriveContentsResult driveContentsResult) {

                // If the operation was not successful, we cannot do anything
                // and must
                // fail.
                if (!driveContentsResult.getStatus().isSuccess()) {
                    //Log.i(TAG, "Failed to create new contents.");
                    Toasty.error(FilesActivity.this, "Failed to create new contents.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Otherwise, we can write our data to the new contents.
                Log.i(TAG, "New contents created.");
                //Toast.makeText(FilesActivity.this, "Picture will be stored to drive.", Toast.LENGTH_SHORT).show();
                // Get an output stream for the contents.

                new Thread() {
                    @Override
                    public void run() {

                        OutputStream outputStream = driveContentsResult.getDriveContents().getOutputStream();

                        try {

                            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            InputStream inputStream = getContentResolver().openInputStream(mFileUri);

                            if (inputStream != null) {
                                byte[] data = new byte[1024];
                                while (inputStream.read(data) != -1) {
                                    outputStream.write(data);
                                }
                                inputStream.close();
                            }

                            outputStream.close();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                        //////////////this is for tittle content
                        File file= new File(mFileUri.getPath());


                        MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                                .setTitle(file.getName())
                                .setMimeType(getMimeType(mFileUri))
                                .setStarred(true).build();

                        IntentSender intentSender = Drive.DriveApi
                                .newCreateFileActivityBuilder()
                                .setInitialMetadata(metadataChangeSet)
                                .setInitialDriveContents(driveContentsResult.getDriveContents())
                                .build(mGoogleApiClient);
                        try {
                            startIntentSenderForResult(
                                    intentSender, REQUEST_CODE_CREATOR, null, 0, 0, 0);
                        } catch (SendIntentException e) {
                            Log.i(TAG, "Failed to launch file chooser.");
                        }

                    }
                }.start();

            }
        });

    }



    public void didTapButton(View view) {

        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);
        animation1.setStartOffset(450);
        animation1.setDuration(400);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }

    public void createFolders(){

        // EXISTING_FOLDER_ID=String.valueOf(result.getDriveFile().getDriveId());

        /////////////////////////////////////Create folder of app if not exist on drive/////////////////////////
        Query query = new Query.Builder()
                .addFilter(Filters.and(Filters.eq(
                        SearchableField.TITLE, "Student,take it easy"),
                        Filters.eq(SearchableField.TRASHED, false)))

                .build();



        Drive.DriveApi.query(getGoogleApiClient(), query)
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(DriveApi.MetadataBufferResult result) {
                        if (!result.getStatus().isSuccess()) {

                            Toasty.error(FilesActivity.this, "Cannot create folder in the root.", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean isFound = false;
                            for(final Metadata m : result.getMetadataBuffer()) {
                                if (m.getTitle().equals("Student,take it easy")) {


                                    DriveId driveId = m.getDriveId();
                                    DriveFolder folder = driveId.asDriveFolder();
                                    appfolderid=driveId;


                                    folder.listChildren(getGoogleApiClient()).setResultCallback(metadataResult);



                                    // Toast.makeText(FilesActivity.this, "Folder of app exists", Toast.LENGTH_SHORT).show();
                                    isFound = true;

                                    break;
                                }
                            }
                            if(!isFound) {
                                Toasty.info(FilesActivity.this, "Creating folder of app...", Toast.LENGTH_SHORT).show();
                                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle("Student,take it easy")
                                        .build();


                                ////Here will be function for creating folders inside app folder
                                Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                        .createFolder(getGoogleApiClient(), changeSet)
                                        .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                                            @Override
                                            public void onResult(DriveFolder.DriveFolderResult result) {
                                                if (!result.getStatus().isSuccess()) {
                                                    Toasty.error(FilesActivity.this, "Error while trying to create the folder", Toast.LENGTH_SHORT).show();

                                                    sFolderId =result.getDriveFolder().getDriveId();



                                                } else {
                                                    sFolderId =result.getDriveFolder().getDriveId();
                                                    Toasty.success(FilesActivity.this, "Created a folder Student,take it easy!", Toast.LENGTH_SHORT).show();

                                                    for (int j = 0; j < subjects.size(); j++) {




                                                        DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), result.getDriveFolder().getDriveId());
                                                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                                                .setTitle(subjects.get(j).getName()).build();
                                                        folder.createFolder(getGoogleApiClient(), changeSet).setResultCallback(folderCreatedCallback);


                                                        Toasty.info(FilesActivity.this,"Creating new folder: " + subjects.get(j).getName()+ "...", Toast.LENGTH_SHORT).show();

                                                        //-------------------------------------------------------------------------------------------------------------------------//

                                                    }


                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
        //----------------------------------------------------------------------------------------------------------------------------------------------------------------//






    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    final ResultCallback<DriveFolder.DriveFolderResult> callback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                Toasty.error(FilesActivity.this, "Error while trying to create the folder", Toast.LENGTH_SHORT).show();
                return;

            }
            Toasty.info(FilesActivity.this, "Created a folder: " + result.getDriveFolder().getDriveId(), Toast.LENGTH_SHORT).show();
        }
    };
    /**
     * Called when the activity will start interacting with the user.
     * At this point your activity is at the top of the activity stack,
     * with user input going to it.
     */

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {

            /**
             * Create the API client and bind it to an instance variable.
             * We use this instance as the callback for connection and connection failures.
             * Since no account name is passed, the user is prompted to choose.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {

            // disconnect Google API client connection
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {


        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());

        if (!result.hasResolution()) {

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */

        try {

            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);

        } catch (SendIntentException e) {

            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }


    /**
     * It invoked when Google API client connected
     * @param connectionHint
     */
    @Override
    public void onConnected(Bundle connectionHint) {

       // Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();


    }


    ///////////////////////Creating subFolders///////////////////////////////////////////////////
    final private ResultCallback<DriveApi.MetadataBufferResult> metadataResult = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                       // showMessage("Problem while retrieving files");
                        Toasty.error(FilesActivity.this, "Problem while retrieving files", Toast.LENGTH_SHORT).show();
                        return;

                    }else
                        subjects = (ArrayList<Subject>) db.getAllSubjects();
                        /*for(int i =0;i<subjects.size();i++)
                        {

                            namesSubject.add(subjects.get(i).getName());

                        }*/
                    List<String> list_subM=new ArrayList<>();
                    for(final Metadata subM : result.getMetadataBuffer()) {

                        list_subM.add(subM.getTitle());


                            }

                    for(int i=0;i<subjects.size();i++) {

                        if (list_subM.contains(subjects.get(i).getName())) {


                            //Toast.makeText(FilesActivity.this, , Toast.LENGTH_SHORT).show();


                        } else {

                                DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), appfolderid);
                                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle(subjects.get(i).getName()).build();
                                folder.createFolder(getGoogleApiClient(), changeSet).setResultCallback(folderCreatedCallback);

                                Toasty.info(FilesActivity.this,"Creating new folder: " + subjects.get(i).getName()+ "...", Toast.LENGTH_SHORT).show();

                            }

                    }

                    }




            };
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------//


////////////////////////////Creating folders///////////////////////////////////////////////////////////
    ResultCallback<DriveFolder.DriveFolderResult> folderCreatedCallback = new
            ResultCallback<DriveFolder.DriveFolderResult>() {
                @Override
                public void onResult(DriveFolder.DriveFolderResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Toasty.error(FilesActivity.this, "Problem while trying to create a folder", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toasty.success(FilesActivity.this, "Folder successfully created!", Toast.LENGTH_SHORT).show();

                }
            };
//-------------------------------------------------------------------------------------------------------//




    /**
     * It invoked when connection suspend
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause) {

        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    public void onClickCreateFile(View view){
        fileOperation = true;

        // create new contents resource
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);



    }



    public void onClickOpenFile(){
        fileOperation = false;

        // create new contents resource
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);
    }

    /**
     *  Open list of folder and file of the Google Drive
     */


    public void OpenFileFromGoogleDrive(){

        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()

                // .setMimeType(new String[] { "text/plain", "text/html" })
                .build(mGoogleApiClient);
        try {
            startIntentSenderForResult(

                    intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);

        } catch (SendIntentException e) {

            Log.w(TAG, "Unable to send intent", e);
        }

    }


    /**
     * This is Result result handler of Drive contents.
     * this callback method call CreateFileOnGoogleDrive() method
     * and also call OpenFileFromGoogleDrive() method, send intent onActivityResult() method to handle result.
     */
    final ResultCallback<DriveContentsResult> driveContentsCallback =


            new ResultCallback<DriveContentsResult>() {
                @Override
                public void onResult(DriveContentsResult result) {

                    if (result.getStatus().isSuccess()) {

                        if (fileOperation == true) {

                            CreateFileOnGoogleDrive(result);


                        } else {

                            OpenFileFromGoogleDrive();

                        }
                    }


                }
            };

    /**
     * Create a file in root folder using MetadataChangeSet object.
     * @param result
     */
    public void CreateFileOnGoogleDrive(DriveContentsResult result){


        final DriveContents driveContents = result.getDriveContents();

        // Perform I/O off the UI thread.
        new Thread() {
            @Override
            public void run() {
                // write content to DriveContents
                OutputStream outputStream = driveContents.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream);
                try {
                    writer.write("Hello abhay!");
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }


                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle("abhaytest2")
                        .setMimeType("text/plain")
                        .setStarred(true).build();


               

                // create a file in root folder
                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                        .createFile(mGoogleApiClient, changeSet, driveContents)
                        .setResultCallback(fileCallback);
            }
        }.start();
    }

    /**
     * Handle result of Created file
     */
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (result.getStatus().isSuccess()) {

                        Toasty.success(getApplicationContext(), "file created: "+""+
                                result.getDriveFile().getDriveId(), Toast.LENGTH_LONG).show();
                        //getGoogleFolders();



                    }

                    return;

                }
            };

    /**
     *  Handle Response of selected file
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_OPENER:

                if (resultCode == RESULT_OK) {

                    mFileId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    Log.e("file id", mFileId.getResourceId() + "");

                    String url = "https://drive.google.com/open?id="+ mFileId.getResourceId();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

                break;

            case REQUEST_CODE_CREATOR:
                // Called after a file is saved to Drive.
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "Image successfully saved.");
                    Toasty.info(this, "File will be stored on drive,may took some time,depend on file size...", Toast.LENGTH_SHORT).show();
                    mBitmapToSave = null;
                    // Just start the camera again for another photo.
                    //startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                            //REQUEST_CODE_CAPTURE_IMAGE);
                }
                break;
            case REQUEST_CODE_STORE_FILE:
                if(resultCode == Activity.RESULT_OK){

                    mFileUri=data.getData();
                    saveFileToDrive();

                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }



    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }


}