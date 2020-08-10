package com.hyphenate.easeui.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.hyphenate.easeui.R;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;
import com.hyphenate.util.UriUtils;
import com.hyphenate.util.VersionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

/**
 * Created by zhangsong on 18-6-6.
 */

public class EaseCompat {
    private static final String TAG = "EaseCompat";

    public static void openImage(Activity context, int requestCode) {
        Intent intent = null;
        if(VersionUtils.isTargetQ(context)) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        context.startActivityForResult(intent, requestCode);
    }

    public static void openImage(Fragment context, int requestCode) {
        Intent intent = null;
        if(VersionUtils.isTargetQ(context.getActivity())) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }else {
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
        }
        intent.setType("image/*");
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * open file
     *
     * @param f
     * @param context
     */
    public static void openFile(File f, Activity context) {
        openFile(context, f);
    }

    /**
     * 打开文件
     * @param context
     * @param filePath
     */
    public static void openFile(Context context, String filePath) {
        if(TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            EMLog.e(TAG, "文件不存在！");
            return;
        }
        openFile(context, new File(filePath));
    }

    /**
     * 打开文件
     * @param context
     * @param file
     */
    public static void openFile(Context context, File file) {
        String filename = file.getName();
        String mimeType = getMimeType(context, file);
        /* get uri */
        Uri uri = getUriForFile(context, file);
        //为了解决本地视频文件打不开的问题
        if(isVideoFile(context, filename)) {
            uri = Uri.parse(file.getAbsolutePath());
        }
        openFile(context, uri, filename, mimeType);
    }

    /**
     * 打开文件
     * @param context
     * @param uri
     */
    public static void openFile(Context context, Uri uri) {
        String filePath = UriUtils.getFilePath(context, uri);
        //如果可以获取文件的绝对路径，则需要根据sdk版本处理FileProvider的问题
        if(!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
            openFile(context, new File(filePath));
            return;
        }
        String filename = UriUtils.getFileNameByUri(context, uri);
        String mimeType = UriUtils.getFileMimeType(context, uri);
        if(TextUtils.isEmpty(mimeType) || TextUtils.equals(mimeType, "application/octet-stream")) {
            mimeType = getMimeType(context, filename);
        }
        openFile(context, uri, filename, mimeType);
    }

    /**
     * 打开文件
     * @param context
     * @param uri 此uri由FileProvider及ContentProvider生成
     * @param filename
     * @param mimeType
     */
    public static void openFile(Context context, Uri uri, String filename, String mimeType) {
        if(openApk(context, uri)) {
            return;
        }
        EMLog.d(TAG, "openFile filename = "+filename + " mimeType = "+mimeType);
        EMLog.d(TAG, "openFile uri = "+ (uri != null ? uri.toString() : "uri is null"));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        setIntentByType(context, filename, intent);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        /* set intent's file and MimeType */
        intent.setDataAndType(uri, mimeType);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            EMLog.e(TAG, e.getMessage());
            Toast.makeText(context, "Can't find proper app to open this file", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 删除文件
     * @param context
     * @param uri
     */
    public static void deleteFile(Context context, Uri uri) {
        if(UriUtils.isFileExistByUri(context, uri)) {
            String filePath = UriUtils.getFilePath(context, uri);
            if(!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                if(file != null && file.exists() && file.isFile()) {
                    file.delete();
                }
            }else {
                try {
                    context.getContentResolver().delete(uri, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Uri getUriForFile(Context context, @NonNull File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static int getSupportedWindowType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            return WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
    }

    /**
     * 获取视频第一帧图片
     * @param context
     * @param videoUri
     * @return
     */
    public static String getVideoThumbnail(Context context, @NonNull Uri videoUri) {
        File file = new File(PathUtil.getInstance().getVideoPath(), "thvideo" + System.currentTimeMillis());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(context, videoUri);
            Bitmap frameAtTime = media.getFrameAtTime();
            frameAtTime.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file.getAbsolutePath();
    }

    /**
     * 用于检查从多媒体获取文件是否是视频
     * @param context
     * @param uri
     * @return
     */
    public static boolean isVideoType(Context context, @NonNull Uri uri) {
        String mimeType = getMimeType(context, uri);
        if(TextUtils.isEmpty(mimeType)) {
            return false;
        }
        return mimeType.startsWith("video");
    }

    /**
     * 用于检查从多媒体获取文件是否是图片
     * @param context
     * @param uri
     * @return
     */
    public static boolean isImageType(Context context, @NonNull Uri uri) {
        String mimeType = getMimeType(context, uri);
        if(TextUtils.isEmpty(mimeType)) {
            return false;
        }
        return mimeType.startsWith("image");
    }

    /**
     * 获取文件mime type
     * @param context
     * @param uri
     * @return
     */
    public static String getMimeType(Context context, @NonNull Uri uri) {
        return context.getContentResolver().getType(uri);
    }

    public static String getMimeType(Context context, @NonNull File file) {
        return getMimeType(context, file.getName());
    }

    public static String getMimeType(Context context, String filename) {
        String mimeType = null;
        Resources resources = context.getResources();
        //先设置常用的后缀

        if(checkSuffix(filename, resources.getStringArray(R.array.ease_image_file_suffix))) {
            mimeType = "image/*";
        }else if(checkSuffix(filename, resources.getStringArray(R.array.ease_video_file_suffix))) {
            mimeType = "video/*";
        }else if(checkSuffix(filename, resources.getStringArray(R.array.ease_audio_file_suffix))) {
            mimeType = "audio/*";
        }else if(checkSuffix(filename, resources.getStringArray(R.array.ease_file_file_suffix))) {
            mimeType = "text/plain";
        }else if(checkSuffix(filename, resources.getStringArray(R.array.ease_word_file_suffix))) {
            mimeType = "application/msword";
        }else if(checkSuffix(filename, resources.getStringArray(R.array.ease_excel_file_suffix))) {
            mimeType = "application/vnd.ms-excel";
        }else if(checkSuffix(filename, resources.getStringArray(R.array.ease_pdf_file_suffix))) {
            mimeType = "application/pdf";
        }else if(checkSuffix(filename, resources.getStringArray(R.array.ease_apk_file_suffix))) {
            mimeType = "application/vnd.android.package-archive";
        }else {
            mimeType = "application/octet-stream";
        }
        return mimeType;
    }

    /**
     * 判断是否是视频文件
     * @param context
     * @param filename
     * @return
     */
    public static boolean isVideoFile(Context context, String filename) {
        return checkSuffix(filename, context.getResources().getStringArray(R.array.ease_video_file_suffix));
    }

    public static void setIntentByType(Context context, String filename, Intent intent) {
        Resources rs = context.getResources();
        if(checkSuffix(filename, rs.getStringArray(R.array.ease_audio_file_suffix))
            || checkSuffix(filename, rs.getStringArray(R.array.ease_video_file_suffix))) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
        }else if(checkSuffix(filename, rs.getStringArray(R.array.ease_image_file_suffix))) {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }else if(checkSuffix(filename, rs.getStringArray(R.array.ease_excel_file_suffix))
                || checkSuffix(filename, rs.getStringArray(R.array.ease_word_file_suffix))
                || checkSuffix(filename, rs.getStringArray(R.array.ease_pdf_file_suffix))) {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }

    public static boolean openApk(Context context, Uri uri) {
        String filename = UriUtils.getFileNameByUri(context, uri);
        return openApk(context, uri, filename);
    }

    public static boolean openApk(Context context, Uri uri, @NonNull String filename) {
        String filePath = UriUtils.getFilePath(context, uri);
        if(filename.endsWith(".apk")) {
            if(TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
                Toast.makeText(context, "Can't find proper app to open this file", Toast.LENGTH_LONG).show();
                return true;
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", new File(filePath));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, getMimeType(context, filename));
                context.startActivity(intent);
            }else {
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                installIntent.setDataAndType(Uri.fromFile(new File(filePath)), getMimeType(context, filename));
                context.startActivity(installIntent);
            }
            return true;
        }
        return false;
    }

    /**
     * 检查后缀
     * @param filename
     * @param fileSuffix
     * @return
     */
    private static boolean checkSuffix(String filename, String[] fileSuffix) {
        if(TextUtils.isEmpty(filename) || fileSuffix == null || fileSuffix.length <= 0) {
            return false;
        }
        int length = fileSuffix.length;
        for(int i = 0; i < length; i++) {
            String suffix = fileSuffix[i];
            if(filename.toLowerCase().endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        return getFilePath(context, uri);
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getFilePath(final Context context, final Uri uri) {
        if(uri == null) {
            return "";
        }
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        //sdk版本在29之前的
        if(!VersionUtils.isTargetQ(context)) {
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
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
            else if(isFileProvider(context, uri)) {
                return getFPUriToPath(context, uri);
            }
            // MediaStore (and general)
            else if (uriStartWithContent(uri)) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if (uriStartWithFile(uri)) {
                return uri.getPath();
            }else if(uri.toString().startsWith("/")) {//如果是路径的话，返回路径
                return uri.toString();
            }
        }else {
            //29之后，判断是否是file开头及是否是以"/"开头
            if(uriStartWithFile(uri)) {
                return uri.getPath();
            }else if(uri.toString().startsWith("/")) {
                return uri.toString();
            }
        }
        return "";
    }

    /**
     * 判断uri是否以file开头
     * @param fileUri
     * @return
     */
    public static boolean uriStartWithFile(Uri fileUri) {
        return "file".equalsIgnoreCase(fileUri.getScheme()) && fileUri.toString().length() > 7;
    }

    /**
     * 判断是否以content开头的Uri
     * @param fileUri
     * @return
     */
    public static boolean uriStartWithContent(Uri fileUri) {
        return "content".equalsIgnoreCase(fileUri.getScheme());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
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
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 从FileProvider获取文件路径
     * @param context
     * @param uri
     * @return
     */
    private static String getFPUriToPath(Context context, Uri uri) {
        try {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs != null) {
                String fileProviderClassName = FileProvider.class.getName();
                for (PackageInfo pack : packs) {
                    ProviderInfo[] providers = pack.providers;
                    if (providers != null) {
                        for (ProviderInfo provider : providers) {
                            if (uri.getAuthority().equals(provider.authority)) {
                                if (provider.name.equalsIgnoreCase(fileProviderClassName)) {
                                    Class<FileProvider> fileProviderClass = FileProvider.class;
                                    try {
                                        Method getPathStrategy = fileProviderClass.getDeclaredMethod("getPathStrategy", Context.class, String.class);
                                        getPathStrategy.setAccessible(true);
                                        Object invoke = getPathStrategy.invoke(null, context, uri.getAuthority());
                                        if (invoke != null) {
                                            String PathStrategyStringClass = FileProvider.class.getName() + "$PathStrategy";
                                            Class<?> PathStrategy = Class.forName(PathStrategyStringClass);
                                            Method getFileForUri = PathStrategy.getDeclaredMethod("getFileForUri", Uri.class);
                                            getFileForUri.setAccessible(true);
                                            Object invoke1 = getFileForUri.invoke(invoke, uri);
                                            if (invoke1 instanceof File) {
                                                String filePath = ((File) invoke1).getAbsolutePath();
                                                return filePath;
                                            }
                                        }
                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isFileProvider(Context context, Uri uri) {
        return (context.getApplicationInfo().packageName + ".fileProvider").equalsIgnoreCase(uri.getAuthority());
    }

}
