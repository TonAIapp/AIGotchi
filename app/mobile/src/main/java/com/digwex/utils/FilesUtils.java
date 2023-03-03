package com.digwex.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.digwex.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.HashMap;

import static android.util.Log.DEBUG;
import static com.digwex.components.log.Log.println;

public class FilesUtils {

  @StringRes
  private static final int INTERNAL_MEMORY_STRING_RES = R.string.internal_memory;
  @StringRes
  private static final int EXTERNAL_MEMORY_STRING_RES = R.string.external_memory;

  @StringRes
  private static final int BYTE_STRING_RES = R.string.size_byte;
  @StringRes
  private static final int KILO_STRING_RES = R.string.size_kilo;
  @StringRes
  private static final int MEGA_STRING_RES = R.string.size_mega;
  @StringRes
  private static final int GIGO_STRING_RES = R.string.size_giga;
  @StringRes
  private static final int TERRA_STRING_RES = R.string.size_terra;

  private static final int[] SIZES = new int[]{BYTE_STRING_RES, KILO_STRING_RES, MEGA_STRING_RES,
    GIGO_STRING_RES, TERRA_STRING_RES};

  public static boolean mkdirsIfNotExists(@NonNull String filepath) {
    return mkdirsIfNotExists(new File(filepath));
  }

  public static boolean mkdirsIfNotExists(@NonNull File file) {
    boolean ok;
    if (!file.exists()) {
      ok = file.mkdirs();
      println(DEBUG, FilesUtils.class, ok ? "Succesed create dir: %s" : "Fail create dir %s",
        file.getPath());
    } else {
      ok = true;
      //println(DEBUG, FilesUtils.class, "Dir exist: %s", file.getPath());
    }
    return ok;
  }

  @NonNull
  public static String getFileExtension(@NonNull String file) {
    return file.substring(file.lastIndexOf("."));
  }

  @NotNull
  public static String partPath(String md5) {
    return md5.substring(0, 2) + '/' + md5.substring(2, 4);
  }

  /**
   * Represents path free space info
   */
  public static class StorageInfo implements Serializable {
    public String path;
    public long free;
    public long total;

    StorageInfo(String path, long free, long total) {
      this.path = path;
      this.free = Math.max(0, free);
      this.total = total;
    }
  }

  /**
   * Returns storages sizes info
   *
   * @param context - application context
   * @return HasMap:
   * key         - String name of storage,
   * value       - StorageInfo storage info
   */
  @NonNull
  public static HashMap<String, StorageInfo> getExternalPaths(@NonNull Context context) {
    HashMap<String, StorageInfo> result = new HashMap<>();

    File[] files = context.getExternalFilesDirs(null);
    if (files != null && files.length > 0) {
      files[0] = Environment.getExternalStorageDirectory();
      for (int i = 0; i < files.length; i++) {
        if (files[i] != null) {
          result.put(context.getString(i == 0
              ? INTERNAL_MEMORY_STRING_RES
              : EXTERNAL_MEMORY_STRING_RES),
            new StorageInfo(files[i].getPath(),
              files[i].getFreeSpace(),
              files[i].getTotalSpace()));
        }
      }
    }
    return result;
  }

  @SuppressLint("DefaultLocale")
  public static String formatSize(Context context, long size, boolean round) {
    float newSize = size;
    int i = 0;

    while (newSize > 1024) {
      newSize /= 1024;
      i++;
    }

    return (round || i == 0 ? String.valueOf(Math.round(newSize)) : String.format("%.2f", newSize)) + " "
      + context.getString(i < SIZES.length ? SIZES[i] : SIZES[0]);
  }

  public static void removeFilesFromDirectory(String dirPath) {
    File dir = new File(dirPath);
    for (File file : dir.listFiles())
      if (!file.isDirectory())
        file.delete();
  }

  private static byte[] createChecksum(String filename) throws Exception {
    InputStream fis = new FileInputStream(filename);

    byte[] buffer = new byte[1024];
    MessageDigest complete = MessageDigest.getInstance("MD5");
    int numRead;

    do {
      numRead = fis.read(buffer);
      if (numRead > 0) {
        complete.update(buffer, 0, numRead);
      }
    } while (numRead != -1);

    fis.close();
    return complete.digest();
  }

  public static String getMD5Checksum(String filename) throws Exception {
    byte[] b = createChecksum(filename);
    StringBuilder result = new StringBuilder();

    for (byte aB : b) {
      result.append(Integer.toString((aB & 0xff) + 0x100, 16).substring(1));
    }
    return result.toString();
  }

}
