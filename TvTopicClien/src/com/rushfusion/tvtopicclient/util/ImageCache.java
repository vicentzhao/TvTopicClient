package com.rushfusion.tvtopicclient.util;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
public class ImageCache {
    private int  hardCacheSize =3*1024;
    //Ӳ����
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
    //������
    private static final int SOFT_CACHE_CAPACITY = 40; 
    private final LinkedHashMap<String, SoftReference<Bitmap>> sSoftImageCache =
             new LinkedHashMap<String, SoftReference<Bitmap>>(40,0.75f,true){
        
         @Override
         protected boolean removeEldestEntry(
                Entry<String, SoftReference<Bitmap>> eldest) {
             if(size()>SOFT_CACHE_CAPACITY){
                Log. d("tag", "�����ÿռ����������һ��" );
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
    //����bitmap,�Ž�Ӳ����
    public boolean putBitmap(String key,Bitmap bitMap){
         if(bitMap!=null){
             synchronized (sHardImageCache ) {
                 sHardImageCache.put(key, bitMap);
            }
             return true ;
        }
         return false ;
    }
    //ȡ�û����bitmap
    public Bitmap getBitmapByCache(String key){
         //��Ӳ�����в�ѯ
         synchronized (sHardImageCache ) {
           Bitmap bitmapcache = sHardImageCache.get(key);
           if(bitmapcache!=null){
               Log. d("image", key+ "==>��Ӳ�����л����б���ѯ��" );
               return bitmapcache;
           } else{
               synchronized (sSoftImageCache ) {
                   SoftReference<Bitmap> reference = sSoftImageCache .get(key);
                   if(reference!=null){
                   Bitmap bitmap = reference.get();
                   if(bitmap!=null){
                       Log. d("image", key+ "==>���������л����б���ѯ��" );
                       return bitmap;
                   } else{
                       Log. d("image", "������ѯ��ͼƬ�ѱ�ɾ��" );
                   }
                   } else{
                       Log. d("image", "���������ڻ�û����Ӧ��ֵ" );
                   }
               }
           }
        }
         //���û�鵽�����������в�ѯ
         return null ;
    }
    //�����ѯ������ ��������
    public Bitmap getBitmapByInternet(String path) throws Exception{
        ImageDownloder  downloder = new ImageDownloder();
        Bitmap bitmap = downloder.imageDownloder(path);
         return bitmap;
    }
}
