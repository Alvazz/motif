package com.uber.motif.sample.lib.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import java.util.Iterator;

public class FileSystem {

    private final Context context;

    public FileSystem(Context context) {
        this.context = context;
    }

    @RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
    public Iterable<Photo> allPhotos() {
        ContentResolver contentResolver = context.getContentResolver();
        String[] columns = new String[] {
                ImageColumns._ID,
                ImageColumns.TITLE,
                ImageColumns.DATE_TAKEN,
                ImageColumns.LATITUDE,
                ImageColumns.LONGITUDE,
                ImageColumns.DATA,
                ImageColumns.MIME_TYPE,
                ImageColumns.SIZE };
        Cursor c = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, null);
        return new PhotoCursor(c);
    }

    private static class PhotoCursor implements Iterable<Photo> {

        private final Cursor c;

        PhotoCursor(Cursor c) {
            this.c = c;
        }

        @NonNull
        @Override
        public Iterator<Photo> iterator() {
            return new Iterator<Photo>() {
                @Override
                public boolean hasNext() {
                    if (c.isClosed()) {
                        return false;
                    }
                    if (c.getCount() == 0) {
                        c.close();
                        return false;
                    }
                    return true;
                }

                @Override
                public Photo next() {
                    c.moveToNext();
                    Photo photo = Photo.fromCursor(c);
                    if (c.isLast() && !c.isClosed()) {
                        c.close();
                    }
                    return photo;
                }
            };
        }
    }
}