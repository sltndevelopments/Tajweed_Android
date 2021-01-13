package ru.tajwid.app.utils.highlight;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Process;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import java.util.ArrayList;
/**
 * This class is used for Parceling Typeface object.
 * Note: Typeface object can not be passed over the process boundary.
 *
 * @hide
 */
public class LeakyTypefaceStorage {
    private static final Object sLock = new Object();
    @GuardedBy("sLock")
    private static final ArrayList<Typeface> sStorage = new ArrayList<>();
    @GuardedBy("sLock")
    private static final ArrayMap<Typeface, Integer> sTypefaceMap=new ArrayMap<>();
    /**
     * Write typeface to parcel.
     *
     * @param typeface A {@link Typeface} to be written.
     * @param parcel A {@link Parcel} object.
     */
    public static void writeTypefaceToParcel(@Nullable Typeface typeface, @NonNull Parcel parcel) {
        parcel.writeInt(Process.myPid());
        synchronized (sLock) {
            final int id;
            final Integer i = sTypefaceMap.get(typeface);
            if (i != null) {
                id = i;
            } else {
                id = sStorage.size();
                sStorage.add(typeface);
                sTypefaceMap.put(typeface, id);
            }
            parcel.writeInt(id);
        }
    }
    /**
     * Read typeface from parcel.
     *
     * If the {@link Typeface} was created in another process, this method returns null.
     *
     * @param parcel A {@link Parcel} object
     * @return A {@link Typeface} object.
     */
    public static @Nullable Typeface readTypefaceFromParcel(@NonNull Parcel parcel) {
        final int pid = parcel.readInt();
        final int typefaceId = parcel.readInt();
        if (pid != Process.myPid()) {
            return null;  // The Typeface was created and written in another process.
        }
        synchronized (sLock) {
            return sStorage.get(typefaceId);
        }
    }
}
