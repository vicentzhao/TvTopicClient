package com.rushfusion.tvtopicclient;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.rushfusion.tvtopicclient.domain.Post;
import com.rushfusion.tvtopicclient.domain.Program;
import com.rushfusion.tvtopicclient.domain.Topic;
import com.rushfusion.tvtopicclient.util.ImageDownloder;
import com.rushfusion.tvtopicclient.util.JSONObject2Post;
import com.rushfusion.tvtopicclient.util.JSONObject2Program;
import com.rushfusion.tvtopicclient.util.JSONObject2Topic;
import com.rushfusion.tvtopicclient.util.JsonUtil;
public class TvTopicClient implements Runnable {
	private ArrayList<Program> programList;
	private ArrayList<Topic> topicList;
	private View topiclistview;
	private ViewHolder holder;
	private ArrayList<Post> postList;
	private ViewFlipper vf;
	private Timer timer;
	private TextView topic_title;
	private Context mContext;
	private ViewGroup mContainer = null;
	private TimerTask imagetask;
	private ImageView tv_homeline_userimage;
	private TextView tv_homeline_username;
	private TextView tv_homeline_topic_comment;
	private TextView tv_homeline_posts_comment;
	private TextView tv_homeline_filmname;
	private HashMap<String, Object> proHashMap;
	private ImageDownloder downloder;
	private boolean isFirstTextLoad = true;
	private final static int ChangeImage = 1;
	private final static int RefalshTextData = 2;
	private final static int RefalshVFData = 3;
	private final static int RefalshListviewData = 4;
	private int mCurrentPhotoIndex = 0;
	private boolean isFirstLoading = true;
	private ListView listview;
	private RelativeLayout rlLoading;
	private JSONArray programJsonArray;
	private boolean isTextOnly = true;
	private boolean isFirstChange = true;
	private JSONArray jsonArray;
	private long oldTimeMillis;
	private StringBuffer showTopic;
	private int animList[] = new int[] { R.anim.push_in_left,
			R.anim.push_out_left, };
	private Button invisview;
	private int visible = View.VISIBLE;
	private int invisible = View.INVISIBLE;
	private int gonevisible = View.GONE;
	private TextView txt_loading;
	private String path = "http://tvsrv.webhop.net:8080/api/programs";
	private Topic topic;
	private JSONArray rejsonArray;
	private int programId;
	private Timer reloadTimer;
	private Timer rlTextTimer;
	private TimerTask rlTextTask;
	private Timer rlvfTimer ;
	private TimerTask rlvfTask;
	private Timer listTimer;
	private TimerTask listTimerTask;
	private int count =8;
	private int page =1;
	private int topicpage =1;
	private int vfpage=1;
	private int vfcount=10;
	private int listpage=1;
	private int listcount=10;
	private boolean isTextProcessbar =false;
	private boolean isVfProcessbar =false;
	private myAdapter  adapter;
	private Button  loadMoreButton;
	private  View footview;
	private RelativeLayout reloading;
	private Button reButton;
	int lastItem = 0;
	int jishiqi=0;
	int vfjishiqi=0;
	private HashMap<String,Object> mparms;
	private boolean isFirstBack=true;
	private ArrayList<Object> taskList;
	private ViewFlipper topic_listflipper;
	private View errorPage;
	private View loadingPage;
	public void run() {
		initData();
		initviewfilpper();
		//这里直接调用asyntask为了初始化一些数据，再次加载不在调用此方法
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected void onPostExecute(Void result) {
				hideProgress(rlLoading);
				setVf();
				invisview.requestFocus();
				invisview.setFocusable(true);
				boolean bbbbbb = invisview.requestFocus();
				invisview.setOnKeyListener(vfKeyListener);
				timer = new Timer();
				if (null != imagetask) {
					imagetask
							.cancel();
				}
				TimerTask imagetask = new TimerTask() {
					public void run() {
						Message message = new Message();
						message.what = ChangeImage;
						handler.sendMessage(message);
					}
				};
				timer.schedule(
						imagetask,
						1000*3, 6*1000);
				super.onPostExecute(result);
			}
			@Override
			protected void onPreExecute() {
				showProgress(rlLoading);
				super.onPreExecute();
			}
			@Override
			protected Void doInBackground(Void... params) {
				getAllProgram();
				programId= getProgramId(mparms);
				getPostList();
				return null;
			}
		}.execute();
		
		
		//监听listview，向下刷新
		listview.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
			   if(position ==topicList.size()){
				   listview.setClickable(false);
				   topicpage=topicpage+1;
				   getTopicList();
				   if(topicList.size()==0){
					   Toast.makeText(mContext, "电影没有更多的话题，重新加载第一页", Toast.LENGTH_SHORT).show();
					   topicpage =1;
					   getTopicList();
					   listview.setAdapter(new myAdapter());
				   }else{
					   listview.setAdapter(new myAdapter());
				   }
			   }
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		    
	}
	 //得到数据库中的所有的电影
	  private void getAllProgram() {
		JsonUtil ju = new JsonUtil();
		String  path ="http://tvsrv.webhop.net:8080/api/programs";
		try {
			 programJsonArray = ju.getSource(path);
			for (int i = 0; i < programJsonArray.length(); i++) {
				   JSONObject jsProgram;
					jsProgram = programJsonArray.getJSONObject(i);
					JSONObject2Program jp = new JSONObject2Program();
					Program program = jp.getProgram(jsProgram);
					programList.add(program);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void setVf() {
			for (int i = 0; i < postList.size(); i++) {
				View initviewfilpper = initviewfilpper();
				initviewfilpper.setVisibility(View.VISIBLE);
				tv_homeline_username.setText(postList.get(i).getUser()
						.getName());
				tv_homeline_topic_comment.setText(postList.get(i).getTopic().getTopic_name());
				String userimagepath = postList.get(i).getUser()
						.getImage();
				tv_homeline_posts_comment.setText(postList.get(i).getC());
				String filmpath = postList.get(i).getTopic()
						.getProgram().getImagePath();
				try {
					Bitmap userBitmap = downloder
							.imageDownloder(userimagepath);
					Bitmap filmBitmap = downloder
							.imageDownloder(filmpath);
					tv_homeline_userimage.setImageBitmap(userBitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}
				vf.addView(initviewfilpper);
			}
	}
	//得到电影的id
	public int getProgramId(HashMap<String, Object> params) {
		int id = 0;
		String channelProgramName = (String) params.get("PROGRAM_NAME");
		
		for (int i = 0; i < programList.size(); i++) {
			Program program = programList.get(i);
			String programName = program.getTitle();
			System.out.println("programName"+programName);
			if (programName.indexOf(channelProgramName) >= 0) {
				id = program.getId();
			}
		}
		return id;
	}
	//initview
	void initData() {
		programList = new ArrayList<Program>();
		topicList = new ArrayList<Topic>();
		downloder = new ImageDownloder();
		programJsonArray =new JSONArray();
		topiclistview = View.inflate(mContext, R.layout.test1, null);
		vf = (ViewFlipper) topiclistview.findViewById(R.id.topic_viewflipper);
		topic_listflipper=(ViewFlipper) topiclistview.findViewById(R.id.topic_listflipper);
		topic_listflipper.setVisibility(View.GONE);
		topic_listflipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
				animList[0]));
		topic_listflipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
				animList[1]));
		txt_loading = (TextView) topiclistview.findViewById(R.id.txt_loading);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
		mContainer.removeAllViews();
		mContainer.addView(topiclistview, lp);
		listview = (ListView) topiclistview.findViewById(R.id.subjectlist);
		invisview = (Button) topiclistview.findViewById(R.id.invisview);
		topiclistview.setFocusable(true);
		topiclistview.requestFocus();
		rlLoading = (RelativeLayout) topiclistview.findViewById(R.id.loading);
		//rlLoading.setVisibility(View.VISIBLE);
		reloading=(RelativeLayout) topiclistview.findViewById(R.id.reloading);
		reButton =(Button) topiclistview.findViewById(R.id.txt_reloading);
		LayoutInflater inflate =LayoutInflater.from(mContext);
//		footview = inflate.inflate(R.layout.footview, null);
//		loadingPage=inflate.inflate(R.layout.loading, null);
		
	}
	//通过progrma
	public void getTopicList() {
				listview.setClickable(true);
		        topicList = new ArrayList<Topic>();
				if (programId == 0) {
					Toast.makeText(mContext, "数据库中没有相应的评论", Toast.LENGTH_SHORT).show();
					Toast.makeText(mContext, "按返回退出", Toast.LENGTH_SHORT).show();
				} else {
					try {
						JsonUtil ju = new JsonUtil();
						String topicPath = "http://tvsrv.webhop.net:8080/api/programs/"
								+ programId + "/topics?page="+topicpage+"&count="+count;
						JSONArray topicJsonArray = ju.getSource(topicPath);
						for (int i = 0; i < topicJsonArray.length(); i++) {
							JSONObject jsTopic = topicJsonArray.getJSONObject(i);
							JSONObject2Topic jt = new JSONObject2Topic();
							Topic topic = jt.getTopic(jsTopic);
							topicList.add(topic);
						}
					} catch (Exception e) {
						e.printStackTrace();
					
					}}
	}
	//拿到相应的post的集合
	public void getPostList() {
		postList = new ArrayList<Post>();
	
		if (programId == 0) {
			System.out.println("programid为空啊");
		} else {
			try {
				JsonUtil ju = new JsonUtil();
				String postPath = "http://tvsrv.webhop.net:8080/api/programs/"
						+ programId + "/posts?page="+topicpage+"&count="+count;
				
				JSONArray PostJsonArray = ju.getSource(postPath);
				for (int i = 0; i < PostJsonArray.length(); i++) {
					JSONObject jsPost = PostJsonArray.getJSONObject(i);
					JSONObject2Post jt = new JSONObject2Post();
					Post post = jt.getPost(jsPost);
					postList.add(post);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}}
	}
    //应用初始化
	public void init(Context context, ViewGroup container,HashMap<String,Object> config) {
		mContext = context;
		mContainer = container;
		mparms =config;
		
		Log.d("tv_topic", "config is:" + config);
		Log.d("tv_topic", "config.Pragram is:" + config.get("PROGRAM_NAME"));
		Log.d("tv_topic", "init has finished");
		
	}
     //用于listview的显示
	public class myAdapter extends BaseAdapter {
		protected int mycount= topicList.size();
		@Override
		public int getCount() {
			System.out.println("topiclist的长度"+topicList.size());
			return topicList.size();
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup viewgroup) {
			holder = new ViewHolder();
			if (convertView == null) {
				LayoutInflater flater = LayoutInflater.from(mContext);
				convertView = flater.inflate(R.layout.listitem, null);
				holder.topic_title = (TextView) convertView
						.findViewById(R.id.topic_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.topic_title.setText(topicList.get(position).getTopic_name());
			return convertView;
		}
	}
   //listview中的控件的初始化
	  public final class ViewHolder {
		public TextView topic_title;
	}
	 //初始化viewfilpper中的控件
	   public View initviewfilpper() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View myviewfillper= inflater.inflate(R.layout.mytopic_2, null);
		tv_homeline_userimage = (ImageView) myviewfillper
				.findViewById(R.id.tv_homeline_userimage);
		tv_homeline_username = (TextView) myviewfillper
				.findViewById(R.id.tv_homeline_username);
		tv_homeline_topic_comment = (TextView) myviewfillper
				.findViewById(R.id.tv_homeline_topic_comment);
		tv_homeline_posts_comment = (TextView) myviewfillper
				.findViewById(R.id.tv_homeline_posts_comment);
		return myviewfillper;
	}
//TODO 
	//发送message，来判断更新数据
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ChangeImage:
				setImageAnimation(vf, postList);
				break;
			case RefalshTextData:
		//		loadMoreTextView();
				break;
			case RefalshVFData:
				loadMoreVfView();
				break;
			case RefalshListviewData:
				loadMoreListview();
				break;
			}
		}
	};
	//设置显示的动画
	private void setImageAnimation(ViewFlipper viewflipper,
			ArrayList<Post> postListfrofillper) {
		viewflipper.clearAnimation();
		mCurrentPhotoIndex = mCurrentPhotoIndex % (postListfrofillper.size());
		viewflipper.clearAnimation();
		viewflipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
				animList[0]));
		viewflipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
				animList[1]));
		viewflipper.showNext();
		mCurrentPhotoIndex++;
	}
    //清除timer
	private void stopTimer(Timer timers,TimerTask tasks) {
		if (timers != null) {
			timers.cancel();
			timers = null;
		}
		if (tasks != null) {
			tasks.cancel();
			tasks = null;
		}
	}

	// 改变左侧话题列表的显示
	private void changeTopicList() {
		if (!isFirstLoading) {
			isFirstLoading = true;
			Toast.makeText(mContext, "这是二次点击", Toast.LENGTH_SHORT).show();
			listview.setVisibility(View.GONE);

		} else {
			listview.setVisibility(View.VISIBLE);
			isFirstLoading = false;
			boolean focus = invisview.requestFocus();
			System.out.println("invisview========>" + focus);
			invisview.setFocusable(true);
		}
	}
	//增加更多的vf
	public void loadMoreVfView() {
		vfpage =vfpage+1;
		int vfvisiable =vf.getVisibility();
		if (vfvisiable == visible) {
			vf.removeAllViews();
			vf.setVisibility(View.GONE);
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected void onPostExecute(Void result) {
					if(rejsonArray.length()==0){
					  vfpage=0;
					  vf.setVisibility(View.VISIBLE);
					  loadMoreVfView();
					}else{
						isVfProcessbar=false;
					for (int i = 0; i < rejsonArray.length(); i++) {
						try {
							JSONObject jsPost = rejsonArray.getJSONObject(i);
							JSONObject2Post jp = new JSONObject2Post();
							Post post = jp.getPost(jsPost);
							postList.add(post);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					for (int i = 0; i < postList.size(); i++) {
						View initviewfilpper = initviewfilpper();
						initviewfilpper.setVisibility(View.VISIBLE);
						tv_homeline_username.setText(postList.get(i).getUser()
								.getName());
						tv_homeline_topic_comment.setText(postList.get(i).getC());
						String userimagepath = postList.get(i).getUser()
								.getImage();
						String filmpath = postList.get(i).getTopic()
								.getProgram().getImagePath();
						try {
							Bitmap userBitmap = downloder
									.imageDownloder(userimagepath);
							Bitmap filmBitmap = downloder
									.imageDownloder(filmpath);
							tv_homeline_userimage.setImageBitmap(userBitmap);
						} catch (Exception e) {
							e.printStackTrace();
						}
						vf.addView(initviewfilpper);
					}
						vf.setVisibility(View.VISIBLE);
						isFirstLoading = false;
					super.onPostExecute(result);
					}
				}
				@Override
				protected void onPreExecute() {
					if(!isVfProcessbar){
					txt_loading.setText("正在加載最新数据");
					isVfProcessbar=true;
					}
					super.onPreExecute();
				}
				@Override
				protected Void doInBackground(Void... params) {
					vfjishiqi++;
					System.out.println("vf sysn执行的次数为===》》"+vfjishiqi);
					int topicId = topic.getId();
					String path = "http://tvsrv.webhop.net:8080/api/topics/"
							+ topicId + "/posts?page="+vfpage+"&count="+vfcount;
					JsonUtil ju = new JsonUtil();
					try {
						rejsonArray = ju.getSource(path);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}.execute();
		}
	}
	//listview进行刷新
	public void loadMoreListview() {
		listpage =listpage+1;
		 int listviewvisiable=listview.getVisibility();
		 if (listviewvisiable == invisible) {
			try {
				JsonUtil ju = new JsonUtil();
				String topicPath = "http://tvsrv.webhop.net:8080/api/programs/"
						+ programId + "/topics?page="+listpage+"&count="+listcount+"";
				JSONArray topicJsonArray = ju.getSource(topicPath);
				if(topicJsonArray.length()==0){
					listpage =0;
					loadMoreListview();
				}else{
				for (int i = 0; i < topicJsonArray.length(); i++) {
					JSONObject jsTopic = topicJsonArray.getJSONObject(i);
					JSONObject2Topic jt = new JSONObject2Topic();
					Topic topic = jt.getTopic(jsTopic);
					topicList.add(topic);
				}
			listview.setAdapter(new myAdapter());
				} 
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void showProgress(RelativeLayout rl) {
		// 把进度对话框显示出来
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		aa.setDuration(1000);
		rl.setAnimation(aa);
		rl.setAnimationCacheEnabled(false);
		rl.setVisibility(View.VISIBLE);
	}

	// 关闭进度条
	public void hideProgress(RelativeLayout rl) {
		AlphaAnimation aa = new AlphaAnimation(1, 0);
		aa.setDuration(2000);
		rl.setAnimation(aa);
		rl.setAnimationCacheEnabled(false);
		rl.setVisibility(View.GONE);
	}
	//为了重复加载和刷新加载，重新加载listview
	class  ListTask extends AsyncTask{

		@Override
		protected void onPreExecute() {
			topic_listflipper.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					topic_listflipper.setVisibility(View.VISIBLE);
				}
			});
			topic_listflipper.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					topic_listflipper.setDisplayedChild(0);
				}
			});
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			listview.setAdapter(new myAdapter());
		    topic_listflipper.setDisplayedChild(1);
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... params) {
			int displayedChild = topic_listflipper.getDisplayedChild();
			int childCount = topic_listflipper.getChildCount();
			System.out.println("displayedChild"+displayedChild+"childCount"+childCount);
			getTopicList();
			return null;
		}
		
		
	}
	//重新加载vf
	class  vfTask extends AsyncTask{
		
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
	View.OnKeyListener vfKeyListener =new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(KeyEvent.KEYCODE_1==keyCode){
				Toast.makeText(mContext, "这是一个测试", 1).show();
				ListTask Listtask = new ListTask();
				Listtask.execute();
			}
			return false;
		}
	};
}