package com.rushfusion.tvtopicclient.util;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
public class ImageCache {
    private int  hardCacheSize =3*1024;
    //硬引用
    LruCache<String, Bitmap> sHardImageCache =new LruCache<String, Bitmap>(hardCacheSize ){
         @Override
         protected int sizeOf(String key, Bitmap value) {
             // TODO Auto-generated method stub
             return super .sizeOf(key, value);
        }

         @Override
         protected void entryRemoved(boolean evicted, String key,
                Bitmap oldValue, Bitmap newValue) {
             Log. v("tag", "hard cache is full , push to soft cache"); 
             sSoftImageCache.put(key, new SoftReference<Bitmap>(oldValue));
        }
    };
    //软引用
    private static final int SOFT_CACHE_CAPACITY = 40; 
    private final LinkedHashMap<String, SoftReference<Bitmap>> sSoftImageCache =
             new LinkedHashMap<String, SoftReference<Bitmap>>(40,0.75f,true){
        
         @Override
         protected boolean removeEldestEntry(
                Entry<String, SoftReference<Bitmap>> eldest) {
             if(size()>SOFT_CACHE_CAPACITY){
                Log. d("tag", "软引用空间已满，清除一个" );
                 return true ;
            }
             return false ;
        }
         @Override
         public SoftReference<Bitmap> put(String key,
                SoftReference<Bitmap> value) {
             return super .put(key, value);
        }
    };
    //缓存bitmap,放进硬引用
    public boolean putBitmap(String key,Bitmap bitMap){
         if(bitMap!=null){
             synchronized (sHardImageCache ) {
                 sHardImageCache.put(key, bitMap);
            }
             return true ;
        }
         return false ;
    }
    //取得缓存的bitmap
    public Bitmap getBitmapByCache(String key){
         //在硬引用中查询
         synchronized (sHardImageCache ) {
           Bitmap bitmapcache = sHardImageCache.get(key);
           if(bitmapcache!=null){
               Log. d("image", key+ "==>在硬引用中缓存中被查询到" );
               return bitmapcache;
           } else{
               synchronized (sSoftImageCache ) {
                   SoftReference<Bitmap> reference = sSoftImageCache .get(key);
                   if(reference!=null){
                   Bitmap bitmap = reference.get();
                   if(bitmap!=null){
                       Log. d("image", key+ "==>在软引用中缓存中被查询到" );
                       return bitmap;
                   } else{
                       Log. d("image", "你所查询的图片已被删除" );
                   }
                   } else{
                       Log. d("image", "软引用现在还没有相应的值" );
                   }
               }
           }
        }
         //如果没查到，在软引用中查询
         return null ;
    }
    //如果查询不到， 开启下载
    public Bitmap getBitmapByInternet(String path) throws Exception{
        ImageDownloder  downloder = new ImageDownloder();
        Bitmap bitmap = downloder.imageDownloder(path);
         return bitmap;
    }
}
