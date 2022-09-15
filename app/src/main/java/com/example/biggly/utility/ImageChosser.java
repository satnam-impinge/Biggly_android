package com.example.biggly.utility;

import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;


import com.example.biggly.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ImageChosser {

    private Uri outputFileUri;
    public File camProfilePic;
    public static final String DOCUMENTS_DIR = "documents";
    public int index= 1;

    public Intent getPickFileChooserIntent(Context context) {

        // Determine Uri of camera image to save.
        outputFileUri = getCaptureImageOutputUri(context);

        List<Intent> allIntents = new ArrayList();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            try {
                Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "My Application Directory");

                if (!path.exists()) {
                    path.mkdir();
                }


             //   camProfilePic = File.createTempFile(context.getResources().getString(R.string.photos)+"_"+index, ".jpg", path);
                camProfilePic = new File(path+"/"+context.getResources().getString(R.string.photos)+"_"+index+ ".jpg");
                camProfilePic.createNewFile();

                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", camProfilePic);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            camProfilePic = new File(Controller.getImagePath(), timeStamp + "post.png");
//            Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", camProfilePic);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
             allIntents.add(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        
        // collect all gallery intents
        // Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //galleryIntent.setType("image/*|application/*");
        // galleryIntent.setType("application/*");
        galleryIntent.setType("*/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        String[] mimetypes = {"image/*", "application/pdf", "application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

        String[] mimeTypes =
                {"image/*", "video/*","application/pdf","application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip", "application/vnd.android.package-archive"};

        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        galleryIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            galleryIntent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
//            if (mimeTypes.length > 0) {
//                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//            }
//        } else {
//            String mimeTypesStr = "";
//            for (String mimeType : mimeTypes) {
//                mimeTypesStr += mimeType + "|";
//            }
//            galleryIntent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
//        }

        allIntents.add(galleryIntent);
//        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
//        for (ResolveInfo res : listGallery) {
//            Intent intent = new Intent(galleryIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//
//            allIntents.add(intent);
//
//        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent() != null && intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(1);

        //allIntents.addAll(allIntents);
        // allIntents.remove(mainIntent);
        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    public Intent getPickImageChooserIntent(Context context) {

        // Determine Uri of camera image to save.
        outputFileUri = getCaptureImageOutputUri(context);

        List<Intent> allIntents = new ArrayList();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);


        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            try{
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "My Application Directory");

            if (!path.exists()) {
                path.mkdir();
            }


                camProfilePic = File.createTempFile(context.getResources().getString(R.string.photos)+"_"+index,".jpg", path);

                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", camProfilePic);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

//            camProfilePic = new File(Controller.getImagePath(), timeStamp + "post.png");
//            Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", camProfilePic);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            allIntents.add(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // collect all gallery intents
       // Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent galleryIntent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //galleryIntent.setType("image/*|application/*");
       // galleryIntent.setType("application/*");
        galleryIntent.setType("image/*");
    //  String[] mimetypes = {"image/*", "video/*","application/pdf", "application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

        String[] mimeTypes =
                {"image/*", "video/*","application/pdf","application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip", "application/vnd.android.package-archive"};

        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        galleryIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            galleryIntent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
//            if (mimeTypes.length > 0) {
//                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//            }
//        } else {
//            String mimeTypesStr = "";
//            for (String mimeType : mimeTypes) {
//                mimeTypesStr += mimeType + "|";
//            }
//            galleryIntent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
//        }

        allIntents.add(galleryIntent);
//        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
//        for (ResolveInfo res : listGallery) {
//            Intent intent = new Intent(galleryIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            allIntents.add(intent);
//
//        }

        allIntents.add(galleryIntent);

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent() != null && intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }

        allIntents.remove(mainIntent);
        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri(Context context) {
        Uri outputFileUri = null;
        File getImage = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    public String getRealPathFromURI(Uri contentUri, Context context) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};


                Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


    public static String getPath(final Context context, final Uri uri) {


        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                return Environment.getExternalStorageDirectory() + "/" + split[1];

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                try {
//                    final String id = DocumentsContract.getDocumentId(uri);
//                    //Log.d(TAG, "getPath: id= " + id);
//                    String[] contentUriPrefixesToTry = new String[]{
//                            "content://downloads/public_downloads",
//                            "content://downloads/my_downloads"
//                    };
//                    for (String contentUriPrefix : contentUriPrefixesToTry) {
//                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
//                        try {
//                            String path = getDataColumn(context, contentUri, null, null);
//                            if (path != null) {
//                                return path;
//                            }
//                        } catch (Exception e) {}
//                    }
//
//                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//                    return getDataColumn(context, contentUri, null, null);
//                }catch (Exception e){
//                    e.printStackTrace();
//                    List<String> segments = uri.getPathSegments();
//                    if(segments.size() > 1) {
//                        String rawPath = segments.get(1);
//                        if(!rawPath.startsWith("/")){
//                            return rawPath.substring(rawPath.indexOf("/"));
//                        }else {
//                            return rawPath;
//                        }
//                    }
//                }
                try {
                    final String id = DocumentsContract.getDocumentId(uri);
                    //Log.d(TAG, "getPath: id= " + id);
                    String[] contentUriPrefixesToTry = new String[]{
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads"
                    };
                    for (String contentUriPrefix : contentUriPrefixesToTry) {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                        try {
                            String path = getDataColumn(context, contentUri, null, null);
                            if (path != null) {
                                return path;
                            }
                        } catch (Exception e) {}
                    }

                    String fileName = getFileName(context, uri);
                    File cacheDir = getDocumentCacheDir(context);
                    File file = generateFileName(fileName, cacheDir);
                    String destinationPath = null;
                    if (file != null) {
                        destinationPath = file.getAbsolutePath();
                        saveFileFromUri(context, uri, destinationPath);
                    }

                    return destinationPath;
                }catch (Exception e){
                    e.printStackTrace();
                    List<String> segments = uri.getPathSegments();
                    if(segments.size() > 1) {
                        String rawPath = segments.get(1);
                        if(!rawPath.startsWith("/")){
                            return rawPath.substring(rawPath.indexOf("/"));
                        }else {
                            return rawPath;
                        }
                    }
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {


                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getUriRealPathAboveKitkat(Context ctx, Uri uri)
    {
        String ret = "";
        if(ctx != null && uri != null) {
            if(isContentUri(uri))
            {
                if(isGooglePhotoDoc(uri.getAuthority()))
                {
                    ret = uri.getLastPathSegment();
                }else {
                    ret = getImageRealPath(ctx.getContentResolver(), uri, null);
                }
            }else if(isFileUri(uri)) {
                ret = uri.getPath();
            }else if(isDocumentUri(ctx, uri)){
                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(uri);
                // Get uri authority.
                String uriAuthority = uri.getAuthority();
                if(isMediaDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        // First item is document type.
                        String docType = idArr[0];
                        // Second item is document real id.
                        String realDocId = idArr[1];
                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if("image".equals(docType))
                        {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        }else if("video".equals(docType))
                        {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        }else if("audio".equals(docType))
                        {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }
                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;
                        ret = getImageRealPath(ctx.getContentResolver(), mediaContentUri, whereClause);
                    }
                }else if(isDownloadDoc(uriAuthority))
                {
                    // Build download uri.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");
                    // Append download document id at uri end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));
                    ret = getImageRealPath(ctx.getContentResolver(), downloadUriAppendId, null);
                }else if(isExternalStoreDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        String type = idArr[0];
                        String realDocId = idArr[1];
                        if("primary".equalsIgnoreCase(type))
                        {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }
        return ret;
    }
    /* Check whether current android os version is bigger than kitkat or not. */
    private boolean isAboveKitKat()
    {
        boolean ret = false;
        ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        return ret;
    }
    /* Check whether this uri represent a document or not. */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isDocumentUri(Context ctx, Uri uri)
    {
        boolean ret = false;
        if(ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri);
        }
        return ret;
    }
    /* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private boolean isContentUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("content".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }
    /* Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private boolean isFileUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("file".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }
    /* Check whether this document is provided by ExternalStorageProvider. Return true means the file is saved in external storage. */
    private boolean isExternalStoreDoc(String uriAuthority)
    {
        boolean ret = false;
        if("com.android.externalstorage.documents".equals(uriAuthority))
        {
            ret = true;
        }
        return ret;
    }
    /* Check whether this document is provided by DownloadsProvider. return true means this file is a downloaed file. */
    private boolean isDownloadDoc(String uriAuthority)
    {
        boolean ret = false;
        if("com.android.providers.downloads.documents".equals(uriAuthority))
        {
            ret = true;
        }
        return ret;
    }
    /*
    Check if MediaProvider provide this document, if true means this image is created in android media app.
    */
    private boolean isMediaDoc(String uriAuthority)
    {
        boolean ret = false;
        if("com.android.providers.media.documents".equals(uriAuthority))
        {
            ret = true;
        }
        return ret;
    }
    /*
    Check whether google photos provide this document, if true means this image is created in google photos app.
    */
    private boolean isGooglePhotoDoc(String uriAuthority)
    {
        boolean ret = false;
        if("com.google.android.apps.photos.content".equals(uriAuthority))
        {
            ret = true;
        }
        return ret;
    }
    /* Return uri represented document file real local path.*/
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause)
    {
        String ret = "";
        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);
        if(cursor!=null)
        {
            boolean moveToFirst = cursor.moveToFirst();
            if(moveToFirst)
            {
                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;
                if( uri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Images.Media.DATA;
                }else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Audio.Media.DATA;
                }else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Video.Media.DATA;
                }
                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);
                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }
        return ret;
    }

    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }

    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
//        logDir(context.getCacheDir());
//        logDir(dir);

        return dir;
    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            //Log.w(TAG, e);
            return null;
        }

        //logDir(directory);

        return file;
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void downloadFile(String url, Context context,String type) {
        String DownloadUrl = url;
        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request1.setDescription("Sample Music File");   //appears the same in Notification bar while downloading
        request1.setTitle(type);
        request1.setVisibleInDownloadsUi(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner();
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        }
        request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/"+type);

        DownloadManager manager1 = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Objects.requireNonNull(manager1).enqueue(request1);
        if (manager1.STATUS_SUCCESSFUL == 8) {
            DownloadSuccess(context);
        }else {
            Downloadfailed(context);
        }
    }

    private void DownloadSuccess(Context context) {

        Toast.makeText(context,context.getResources().getString(R.string.app_name),Toast.LENGTH_SHORT).show();
    }
    private void Downloadfailed(Context context) {

        Toast.makeText(context,context.getResources().getString(R.string.app_name),Toast.LENGTH_SHORT).show();
    }
    public  String getPath1(final Context context, final Uri uri) {


        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                return Environment.getExternalStorageDirectory() + "/" + split[1];

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                try {
//                    final String id = DocumentsContract.getDocumentId(uri);
//                    //Log.d(TAG, "getPath: id= " + id);
//                    String[] contentUriPrefixesToTry = new String[]{
//                            "content://downloads/public_downloads",
//                            "content://downloads/my_downloads"
//                    };
//                    for (String contentUriPrefix : contentUriPrefixesToTry) {
//                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
//                        try {
//                            String path = getDataColumn(context, contentUri, null, null);
//                            if (path != null) {
//                                return path;
//                            }
//                        } catch (Exception e) {}
//                    }
//
//                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//                    return getDataColumn(context, contentUri, null, null);
//                }catch (Exception e){
//                    e.printStackTrace();
//                    List<String> segments = uri.getPathSegments();
//                    if(segments.size() > 1) {
//                        String rawPath = segments.get(1);
//                        if(!rawPath.startsWith("/")){
//                            return rawPath.substring(rawPath.indexOf("/"));
//                        }else {
//                            return rawPath;
//                        }
//                    }
//                }
                try {
                    final String id = DocumentsContract.getDocumentId(uri);
                    //Log.d(TAG, "getPath: id= " + id);
                    String[] contentUriPrefixesToTry = new String[]{
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads"
                    };
                    for (String contentUriPrefix : contentUriPrefixesToTry) {
                        try {
                            Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));

                            String path = getDataColumn(context, contentUri, null, null);
                            if (path != null) {
                                return path;
                            }
                        } catch (Exception e) {

                        }
                    }

                    String fileName = getFileName(context, uri);
                    File cacheDir = getDocumentCacheDir(context);
                    File file = generateFileName(fileName, cacheDir);
                    String destinationPath = null;
                    if (file != null) {
                        destinationPath = file.getAbsolutePath();
                        saveFileFromUri(context, uri, destinationPath);
                    }

                    return destinationPath;
                }catch (Exception e){
                    e.printStackTrace();
                    List<String> segments = uri.getPathSegments();
                    if(segments.size() > 1) {
                        String rawPath = segments.get(1);
                        if(!rawPath.startsWith("/")){
                            return rawPath.substring(rawPath.indexOf("/"));
                        }else {
                            return rawPath;
                        }
                    }
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
