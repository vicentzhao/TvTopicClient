package com.rushfusion.tvtopicclient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.rushfusion.tvtopicclient.domain.Post;
import com.rushfusion.tvtopicclient.domain.Program;
import com.rushfusion.tvtopicclient.domain.Topic;
import com.rushfusion.tvtopicclient.util.ImageCache;
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
	private LinearLayout errorLinelayout;
	private Button errorreloading;
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
	private Timer rlvfTimer;
	private TimerTask rlvfTask;
	private Timer listTimer;
	private TimerTask listTimerTask;
	private int count = 8;
	private int page = 1;
	private int topicpage = 1;
	private int vfpage = 1;
	private int vfcount = 10;
	private int listpage = 1;
	private int listcount = 10;
	private boolean isTextProcessbar = false;
	private boolean isVfProcessbar = false;
	private myAdapter mAdapter;
	private Button loadMoreButton;
	private View footview;
	private Topic currenTopic;
	private RelativeLayout reloading;
	private Button reButton;
	int lastItem = 0;
	int jishiqi = 0;
	int vfjishiqi = 0;
	private HashMap<String, Object> mparms;
	private boolean isFirstBack = true;
	private ArrayList<Object> taskList;
	private View errorPage;
	private View loadingPage;
	private LinearLayout viewfillper;
	private long startTimer;
	private vfTask vtask;
	private ListTask listTask;
	private final static int ShowTopicList =5;
	private final static int ShowLoadingPage =6;
	private final static int autoReflush =7;
	private final static int autoReflushList =8;
	private final static int autoReflushTopicList  =9;
	private final static int autoReflushQPostList  =10;
	int cheshii =0;
	private ImageCache imageCache;
	private boolean isFirstPostLoading =true;
	private View footviewNoMore;
	myTopicListAdd myAddTask;
	int LoadMoreSize =10;
	int Itemtopicpage=1;
	private LinearLayout rlayoutLoading;
	ArrayList<Topic> myTopicList = new  ArrayList<Topic>();
	private autoReflushPostView autoReflushPostViewTask;
	private reflushPostView reflushPostViewTask;
	public void run() {
		initData();
		initviewfilpper();
		startTimer =System.currentTimeMillis();
		imageCache = new ImageCache();
		autoReflushTopicList();
		if(autoReflushPostViewTask!=null){
			reflushPostViewTask.cancel(true);
			reflushPostViewTask=new reflushPostView();
			reflushPostViewTask.execute();
			reflushPostViewTask=null;
		}else{
			reflushPostViewTask=new reflushPostView();
			reflushPostViewTask.execute();	
			reflushPostViewTask=null;
		}
		autoReflushErrorPost();
		autoReflushPostList();
		// 监听listview，向下刷新
		listview.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("onitemselected postion ===="+position);
				System.out.println("topicList.size()=======>"+topicList.size());
				if (position == myTopicList.size()) {
					listview.setClickable(false);
					int visibility = footviewNoMore.getVisibility();
					if(visibility!=View.VISIBLE){
					if(LoadMoreSize<count){
						return ;
					}else{
					topicpage = topicpage + 1;
					if(null!=myAddTask){
						myAddTask.cancel(true);
						myAddTask = new myTopicListAdd();
						myAddTask.execute();
						myAddTask=null;
					}else{
						myAddTask = new myTopicListAdd();
						myAddTask.execute();
						myAddTask=null;
					}
					}
				}
				//	topicList=new ArrayList<Topic>();
				}else{
					listview.setClickable(true);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		listview.setOnItemClickListener(myItemClick);
		reButton.setOnClickListener(reflushListener);
		listview.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_2){
					System.out.println("listview的listener已经被监听到");
					System.currentTimeMillis();
					System.out.println(System.currentTimeMillis());
					long dis = getDis();
					if(dis>1000){
						viewfillper.setVisibility(View.GONE);
						invisview.requestFocus();
						invisview.setFocusable(true);
					}
				}
				if(keyCode==KeyEvent.KEYCODE_1){
					System.currentTimeMillis();
					System.out.println(System.currentTimeMillis());
					long dis = getDis();
					if(dis>1000){
						if(null!=listTask){
							listTask.cancel(true);
							listTask=new ListTask();
							listTask.execute();
							listTask=null;
						}else{
							listTask=new ListTask();
							listTask.execute();
							listTask=null;
						}
						
					}
				}
				return false;
			}
		});
		errorreloading.setOnClickListener(errorreloadingListener);
	}
	// 得到数据库中的所有的电影
	private void getAllProgram() {
		JsonUtil ju = new JsonUtil();
		String path = "http://tvsrv.webhop.net:8080/api/programs";
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
		    vf.removeAllViews();
		for (int i = 0; i < postList.size(); i++) {
			View initviewfilpper = initviewfilpper();
			initviewfilpper.setVisibility(View.VISIBLE);
			tv_homeline_username.setText(postList.get(i).getUser().getName());
			tv_homeline_topic_comment.setText(postList.get(i).getTopic()
					.getTopic_name());
			String userimagepath = postList.get(i).getUser().getImage();
			tv_homeline_posts_comment.setText(postList.get(i).getC());
			String filmpath = postList.get(i).getTopic().getUser().getImage();
			Bitmap bitmapByCache = imageCache.getBitmapByCache(filmpath);
			try {
			if(bitmapByCache!=null){
				tv_homeline_userimage.setImageBitmap(bitmapByCache);
			}
			else{
				Bitmap bitmapByInternet = imageCache.getBitmapByInternet(filmpath);
				if(bitmapByInternet!=null){
					tv_homeline_userimage.setImageBitmap(bitmapByInternet);
					imageCache.putBitmap(filmpath, bitmapByInternet);
				}
			}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			vf.addView(initviewfilpper);
		}
	}

	// 得到电影的id
	public int getProgramId(HashMap<String, Object> params) {
		int id = 0;
		String channelProgramName = (String) params.get("PROGRAM_NAME");

		for (int i = 0; i < programList.size(); i++) {
			Program program = programList.get(i);
			String programName = program.getTitle();
			System.out.println("programName" + programName);
			if (programName.indexOf(channelProgramName) >= 0) {
				id = program.getId();
			}
		}
		return id;
	}

	// initview
	void initData() {
		programList = new ArrayList<Program>();
		topicList = new ArrayList<Topic>();
		postList=new ArrayList<Post>();
		downloder = new ImageDownloder();
		mAdapter=new myAdapter();
		programJsonArray = new JSONArray();
		topiclistview = View.inflate(mContext, R.layout.test1, null);
		errorreloading=(Button) topiclistview.findViewById(R.id.errorreloading);
		errorLinelayout=(LinearLayout) topiclistview.findViewById(R.id.errorLinelayout);
		vf = (ViewFlipper) topiclistview.findViewById(R.id.topic_viewflipper);
		viewfillper = (LinearLayout) topiclistview
				.findViewById(R.id.viewfillper);
		rlayoutLoading = (LinearLayout) topiclistview.findViewById(R.id.viewfillperloading);
		viewfillper.setVisibility(View.GONE);
		txt_loading = (TextView) topiclistview.findViewById(R.id.txt_loading);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
		mContainer.removeAllViews();
		mContainer.addView(topiclistview, lp);
		listview = (ListView) topiclistview.findViewById(R.id.subjectlist);
		LayoutInflater inflate = LayoutInflater.from(mContext);
		footview = inflate.inflate(R.layout.footview, null);
		footviewNoMore=inflate.inflate(R.layout.footview1, null);
	
		invisview = (Button) topiclistview.findViewById(R.id.invisview);
		topiclistview.requestFocus();
		topiclistview.setFocusable(true);
		rlLoading = (RelativeLayout) topiclistview.findViewById(R.id.loading);
		// rlLoading.setVisibility(View.VISIBLE);
		reloading = (RelativeLayout) topiclistview.findViewById(R.id.reloading);
		reloading.getBackground().setAlpha(50);
		reButton = (Button) topiclistview.findViewById(R.id.txt_reloading);
		// loadingPage=inflate.inflate(R.layout.loading, null);
//		listview.addFooterView(footview);
//		footview.setVisibility(View.VISIBLE);
		listview.setAdapter(mAdapter);
	}
	// 通过progrma
	public ArrayList<Topic> getTopicList() {
		listview.setClickable(true);
		topicList = new ArrayList<Topic>();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (programId == 0) {
			Toast.makeText(mContext, "数据库中没有相应的评论", Toast.LENGTH_SHORT).show();
			Toast.makeText(mContext, "按返回退出", Toast.LENGTH_SHORT).show();
		} else {
			try {
				JsonUtil ju = new JsonUtil();
				String topicPath = "http://tvsrv.webhop.net:8080/api/programs/"
						+ programId + "/topics?page=" + topicpage + "&count="
						+ count;
				JSONArray topicJsonArray = ju.getSource(topicPath);
				System.out.println("topicpath=="+topicPath);
				for (int i = 0; i < topicJsonArray.length(); i++) {
					JSONObject jsTopic = topicJsonArray.getJSONObject(i);
					JSONObject2Topic jt = new JSONObject2Topic();
					Topic topic = jt.getTopic(jsTopic);
					topicList.add(topic);
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return topicList;
	}

	// 拿到相应的post的集合
	public void getPostList(String postPath) {
		postList = new ArrayList<Post>();

		if (programId == 0) {
			System.out.println("programid为空啊");
		} else {
			try {
				JsonUtil ju = new JsonUtil();
				JSONArray PostJsonArray = ju.getSource(postPath);
				for (int i = 0; i < PostJsonArray.length(); i++) {
					JSONObject jsPost = PostJsonArray.getJSONObject(i);
					JSONObject2Post jt = new JSONObject2Post();
					Post post = jt.getPost(jsPost);
					postList.add(post);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 应用初始化
	public void init(Context context, ViewGroup container,
			HashMap<String, Object> config) {
		mContext = context;
		mContainer = container;
		mparms = config;

		Log.d("tv_topic", "config is:" + config);
		Log.d("tv_topic", "config.Pragram is:" + config.get("PROGRAM_NAME"));
		Log.d("tv_topic", "init has finished");

	}

	// 用于listview的显示
	public class myAdapter extends BaseAdapter {
		protected int mycount = topicList.size();
		private ArrayList<Topic> mylist = new ArrayList<Topic>();

		public ArrayList getList() {
			return mylist;
		}

		public void setList(ArrayList list) {
			this.mylist = list;
		}
		@Override
		public int getCount() {
			return mylist.size();
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
			System.out.println("getview 我又被执行了");
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
			if(mylist.size()!=0){
			String title =mylist.get(position).getTopic_name();
			if(title!=null){
			holder.topic_title.setText(title);
			}
			}
			return convertView;
		}
	}

	// listview中的控件的初始化
	public final class ViewHolder {
		public TextView topic_title;
	}

	// 初始化viewfilpper中的控件
	public View initviewfilpper() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View myviewfillper = inflater.inflate(R.layout.mytopic_2, null);
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

	// TODO
	// 发送message，来判断更新数据
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ChangeImage:
				if(postList.size()!=0){
				setImageAnimation(vf, postList);
				}else{
					reButton.setVisibility(View.VISIBLE);
					System.out.println("postlist is size"+postList.size());
				}
				break;
			case RefalshTextData:
				// loadMoreTextView();
				break;
			case RefalshVFData:
				loadMoreVfView();
				break;
			case ShowLoadingPage:
				break;
			case ShowTopicList:
				if(postList.size()!=0){
				mAdapter.notifyDataSetChanged();
				listview.setVisibility(View.VISIBLE);
				}
				else{
					errorLinelayout.setVisibility(View.VISIBLE);
				}
				break;
			case RefalshListviewData:
			//	loadMoreListview();
				break;
			case autoReflushList:
				int v = errorLinelayout.getVisibility();
				if(v==View.VISIBLE){
					errorLinelayout.setVisibility(View.GONE);
				if(listTask!=null){
					listTask.cancel(true);
				listTask=new ListTask();
				listTask.execute();
				}else{
					listTask=new ListTask();
					listTask.execute();	
				}
				}
				break;
			case autoReflush:
				if(postList.size()==0){
					int visibility = reloading.getVisibility();
					if(visibility==View.VISIBLE){
						reloading.setVisibility(View.GONE);
						if(isFirstPostLoading){
							if(autoReflushPostViewTask!=null){
								reflushPostViewTask.cancel(true);
								reflushPostViewTask=new reflushPostView();
								reflushPostViewTask.execute();
								reflushPostViewTask=null;
							}else{
								reflushPostViewTask=new reflushPostView();
								reflushPostViewTask.execute();	
								reflushPostViewTask=null;
							}
						}else{
							if(autoReflushPostViewTask!=null){
								autoReflushPostViewTask.cancel(true);
								autoReflushPostViewTask=new autoReflushPostView();
								autoReflushPostViewTask.execute();
								autoReflushPostViewTask=null;
							}else{
								autoReflushPostViewTask=new autoReflushPostView();
								autoReflushPostViewTask.execute();	
								autoReflushPostViewTask=null;
							}
							
						}
					}
				}
				break;
			case autoReflushQPostList:
				if(isFirstPostLoading){
					if(autoReflushPostViewTask!=null){
						reflushPostViewTask.cancel(true);
						reflushPostViewTask=new reflushPostView();
						reflushPostViewTask.execute();
						reflushPostViewTask=null;
					}else{
						reflushPostViewTask=new reflushPostView();
						reflushPostViewTask.execute();	
						reflushPostViewTask=null;
					}
				}else{
					if(autoReflushPostViewTask!=null){
						autoReflushPostViewTask.cancel(true);
						autoReflushPostViewTask=new autoReflushPostView();
						autoReflushPostViewTask.execute();
						autoReflushPostViewTask=null;
					}else{
						autoReflushPostViewTask=new autoReflushPostView();
						autoReflushPostViewTask.execute();	
						autoReflushPostViewTask=null;
					}
					
				}
				break;
			case autoReflushTopicList:
				int listvisivility = listview.getVisibility();
				if(listvisivility==View.VISIBLE){
					if(listTask!=null){
						listTask.cancel(true);
					listTask=new ListTask();
					listTask.execute();
					listTask=null;
					}else{
						listTask=new ListTask();
						listTask.execute();	
						listTask=null;
					}
				}
				break;
			}
		}
	};

	// 设置显示的动画
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

	// 清除timer
	private void stopTimer(Timer timers, TimerTask tasks) {
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

	// 增加更多的vf
	public void loadMoreVfView() {
		vfpage = vfpage + 1;
		int vfvisiable = vf.getVisibility();
		if (vfvisiable == visible) {
			vf.removeAllViews();
			vf.setVisibility(View.GONE);
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected void onPostExecute(Void result) {
					if (rejsonArray.length() == 0) {
						vfpage = 0;
						vf.setVisibility(View.VISIBLE);
						loadMoreVfView();
					} else {
						isVfProcessbar = false;
						for (int i = 0; i < rejsonArray.length(); i++) {
							try {
								JSONObject jsPost = rejsonArray
										.getJSONObject(i);
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
							tv_homeline_username.setText(postList.get(i)
									.getUser().getName());
							tv_homeline_topic_comment.setText(postList.get(i)
									.getC());
							String userimagepath = postList.get(i).getUser()
									.getImage();
							String filmpath = postList.get(i).getTopic()
									.getProgram().getImagePath();
							try {
								Bitmap userBitmap = downloder
										.imageDownloder(userimagepath);
								Bitmap filmBitmap = downloder
										.imageDownloder(filmpath);
								tv_homeline_userimage
										.setImageBitmap(userBitmap);
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
					if (!isVfProcessbar) {
						txt_loading.setText("正在加載最新数据");
						isVfProcessbar = true;
					}
					super.onPreExecute();
				}

				@Override
				protected Void doInBackground(Void... params) {
					vfjishiqi++;
					System.out.println("vf sysn执行的次数为===》》" + vfjishiqi);
					int topicId = topic.getId();
					String path = "http://tvsrv.webhop.net:8080/api/topics/"
							+ topicId + "/posts?page=" + vfpage + "&count="
							+ vfcount;
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

	// listview进行刷新
//	public void loadMoreListview() {
//		listpage = listpage + 1;
//		int listviewvisiable = listview.getVisibility();
//		if (listviewvisiable == invisible) {
//			try {
//				JsonUtil ju = new JsonUtil();
//				String topicPath = "http://tvsrv.webhop.net:8080/api/programs/"
//						+ programId + "/topics?page=" + listpage + "&count="
//						+ listcount + "";
//				JSONArray topicJsonArray = ju.getSource(topicPath);
//				if (topicJsonArray.length() == 0) {
//					listpage = 0;
//					loadMoreListview();
//				} else {
//					for (int i = 0; i < topicJsonArray.length(); i++) {
//						JSONObject jsTopic = topicJsonArray.getJSONObject(i);
//						JSONObject2Topic jt = new JSONObject2Topic();
//						Topic topic = jt.getTopic(jsTopic);
//						topicList.add(topic);
//					}
//					listview.setAdapter(new myAdapter());
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

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
	// 为了重复加载和刷新加载，重新加载listview,第一次进入时加载
	class ListTask extends AsyncTask {
				@Override
		protected void onPreExecute() {
			LoadMoreSize=10;
			myTopicList =new ArrayList<Topic>();
			System.out.println("listtask我已经执行了");
			viewfillper.setVisibility(View.VISIBLE);
			rlayoutLoading.setVisibility(View.VISIBLE);
			listview.removeFooterView(footviewNoMore);
			listview.removeFooterView(footview);
			footview.setVisibility(View.GONE);
			footviewNoMore.setVisibility(View.GONE);
			listview.addFooterView(footview);
			footview.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Object result) {
			if(topicList.size()!=0){
			rlayoutLoading.setVisibility(View.GONE);
			listview.setVisibility(View.GONE);
			mAdapter.setList(topicList);
	//	    mAdapter.notifyDataSetChanged();
			listview.setAdapter(mAdapter);
			listview.setVisibility(View.VISIBLE);
			listview.requestFocus();
			listview.setFocusable(true);
			for (int i = 0; i < topicList.size(); i++) {
				myTopicList.add(topicList.get(i));
			}
			}else{
				rlayoutLoading.setVisibility(View.GONE);
				errorLinelayout.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}
		@Override
		protected Object doInBackground(Object... params) {
			 getTopicList();
			return null;
		}
	}
	// 下拉进行刷新数据
	class myTopicListAdd extends AsyncTask {
		protected void onPostExecute(Object result) {
			if (topicList.size() == 0) {
				LoadMoreSize =topicList.size();
				mAdapter.setList(myTopicList);
				mAdapter.notifyDataSetChanged();
				listview.removeFooterView(footview);
				footview.setVisibility(View.GONE);
				listview.addFooterView(footviewNoMore);
				footviewNoMore.setVisibility(View.VISIBLE);
				topicpage=1;
				return;
//				listview.removeFooterView(footview);
//				listview.addFooterView(footviewNoMore);
//				topicList =myTopicList;
//				mAdapter.notifyDataSetChanged();
//				listview.requestFocus();
//				listview.setFocusable(true);
			}else if((topicList.size()<count)){
					LoadMoreSize =topicList.size();
					listview.removeFooterView(footview);
					footview.setVisibility(View.GONE);
					listview.addFooterView(footviewNoMore);
					footviewNoMore.setVisibility(View.VISIBLE);
					topicpage=1;
				for (int i = 0; i < topicList.size(); i++) {
					Topic t =topicList.get(i);
					myTopicList.add(t);
				}
				mAdapter.setList(myTopicList);
			listview.setVisibility(View.GONE);
			mAdapter.notifyDataSetChanged();
			listview.setVisibility(View.VISIBLE);
			listview.requestFocus();
			listview.setFocusable(true);
			}
			else if(topicList.size()==count){
				LoadMoreSize =topicList.size();
				for (int i = 0; i < topicList.size(); i++) {
					myTopicList.add(topicList.get(i));
				}
				mAdapter.setList(myTopicList);
				listview.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
				listview.setVisibility(View.VISIBLE);
				listview.requestFocus();
				listview.setFocusable(true);
			}
			super.onPostExecute(result);
		}
		@Override
		protected void onPreExecute() {
			if(topicList.size()<count){
				LoadMoreSize =topicList.size();
				listview.removeFooterView(footview);
				footview.setVisibility(View.GONE);
				listview.addFooterView(footviewNoMore);
				footviewNoMore.setVisibility(View.VISIBLE);
				myAddTask.cancel(true);
			}
			super.onPreExecute();
		}
		@Override
		protected Object doInBackground(Object... params) {
			if(topicList.size()>=count){
			getTopicList();
			}
			return null;
		}
	}
	// 重新加载vf
	class vfTask extends AsyncTask {
		@Override
		protected void onPreExecute() {
			showProgress(rlLoading);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			hideProgress(rlLoading);
			vf.setVisibility(View.VISIBLE);
			if(postList.size()!=0){
			setVf();
			setPostTimerStart();
			}else{
				Toast.makeText(mContext, "这个话题没有评论", 1).show();
				reButton.setVisibility(View.VISIBLE);
				reButton.requestFocus();
				reButton.setFocusable(true);
			}
			super.onPostExecute(result);
		}
		@Override
		protected Object doInBackground(Object... params) {
			String path =(String) params[0];
			getPostList(path);
			return null;
		}
	}
	View.OnClickListener  reflushListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			reloading.setVisibility(View.GONE);
			if(autoReflushPostViewTask!=null){
				reflushPostViewTask.cancel(true);
				reflushPostViewTask=new reflushPostView();
				reflushPostViewTask.execute();
				reflushPostViewTask=null;
			}else{
				reflushPostViewTask=new reflushPostView();
				reflushPostViewTask.execute();	
				reflushPostViewTask=null;
			}
		}
	};
	OnClickListener errorreloadingListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(listTask!=null){
				listTask.cancel(true);
			listTask=new ListTask();
			listTask.execute();
			listTask=null;
			}else{
				listTask=new ListTask();
				listTask.execute();	
				listTask=null;
			}
		}
	};
	View.OnKeyListener vfKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (KeyEvent.KEYCODE_1 == keyCode) {
				System.currentTimeMillis();
				System.out.println(System.currentTimeMillis());
				long dis = getDis();
				if(dis>1000){
					if(listTask!=null){
						listTask.cancel(true);
					listTask=new ListTask();
					listTask.execute();
					}else{
						listTask=new ListTask();
						listTask.execute();	
					}
//				Toast.makeText(mContext, "你已经被点击了", 1).show();
//				System.out.println("你被点击了"+cheshii);
//				cheshii++;
//				viewfillper.setVisibility(View.VISIBLE);
//				topic_listflipper.setVisibility(View.VISIBLE);
//				topic_listflipper.setDisplayedChild(0);
//				int displayedChild = topic_listflipper.getDisplayedChild();
//				ListTask Listtask = new ListTask();
//				Listtask.execute();
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						getTopicList();
//						Message msg = new Message();
//						msg.what=ShowTopicList;
//						handler.handleMessage(msg);
//					}
//				}).run();
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						Message msgloading =new Message();
//						msgloading.what=ShowLoadingPage;
//						handler.handleMessage(msgloading);
//					}
//				}).run();
			}
			}
			return false;
		}
	};
	// 对listview的item点击进行响应
	OnItemClickListener myItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			long currenttimer=System.currentTimeMillis();
			long distime =currenttimer-startTimer;
			System.out.println("distime===========>"+distime);
			if(distime>1000){
			isFirstPostLoading =false;
			startTimer=currenttimer;
			stopTimer(timer, imagetask);
			currenTopic =myTopicList.get(position);
			 int currenTopicid = myTopicList.get(position).getId();
			System.out.println("topicid wei"+currenTopicid);
			System.out.println("topicList.size()=======>"+topicList.size());
			String topicpath = "http://tvsrv.webhop.net:8080/api/topics/"
					+ currenTopicid + "/posts?page=" + Itemtopicpage + "&count=" + count;
			if (vtask != null) {
				vtask.cancel(true);
				vtask = new vfTask();
				vtask.execute(topicpath);
				vtask=null;
			} else {
				vf.setVisibility(View.GONE);
				stopTimer(timer, imagetask);
				vtask = new vfTask();
				vtask.execute(topicpath);
			}
		}else{
			int i=0;
			i++;
			Toast.makeText(mContext, "你点击太平凡了"+i, 0);
		}
		}
	};
	private void setPostTimerStart() {
		timer = new Timer();
		if (null != imagetask) {
			imagetask.cancel();
		}
		 imagetask = new TimerTask() {
			public void run() {
				Message message = new Message();
				message.what = ChangeImage;
				handler.sendMessage(message);
			}
		};
		timer.schedule(imagetask, 1000 * 3, 6 * 1000);
	}
	//判断两个动作的间隔时间
	public long getDis(){
		long currenttimer=System.currentTimeMillis();
		long distime =currenttimer-startTimer;
		System.out.println("distime===========>"+distime);
		startTimer=currenttimer;
		return distime;
	}
	//第一次進入時執行的asyn方法，還有刷新的時候執行
	class reflushPostView extends AsyncTask {
		// 这里直接调用asyntask为了初始化一些数据，再次加载不在调用此方法
					@Override
					protected void onPostExecute(Object result) {
						hideProgress(rlLoading);
						if(postList.size()!=0){
						setVf();
						vf.setVisibility(View.VISIBLE);
						invisview.requestFocus();
						invisview.setFocusable(true);
						boolean bbbbbb = invisview.requestFocus();
						invisview.setOnKeyListener(vfKeyListener);
						setPostTimerStart();
						}else{
							vf.setVisibility(View.GONE);
							reloading.setVisibility(View.VISIBLE);
							reButton.requestFocus();
							reButton.setFocusable(true);
						}
						super.onPostExecute(result);
					}
					//開啟定時循環展示
					@Override
					protected void onPreExecute() {
						showProgress(rlLoading);
						reloading.setVisibility(View.GONE);
						super.onPreExecute();
					}
					@Override
					protected Void doInBackground(Object... params) {
						getAllProgram();
						programId = getProgramId(mparms);
						if(programId!=0){
						 String BeginPath = "http://tvsrv.webhop.net:8080/api/programs/"
								+ programId + "/posts?page=" + topicpage + "&count=" + count;
						getPostList(BeginPath);
						}
						return null;
					}
	}
	public void autoReflushErrorPost(){
		Timer autotimer = new Timer();
		TimerTask autotimertask = new TimerTask() {
			public void run() {
				Message message = new Message();
				message.what = autoReflush;
				handler.sendMessage(message);
				Message msg =new Message();
				msg.what=autoReflushList;
				handler.handleMessage(msg);
			}
		};
		autotimer.schedule(autotimertask, 1000 * 5, 10 * 1000);
	}
	public void autoReflushTopicList(){
		Timer autotimer = new Timer();
		TimerTask autotimertask = new TimerTask() {
			public void run() {
				Message message = new Message();
				message.what = autoReflushTopicList;
				handler.sendMessage(message);
			}
		};
		autotimer.schedule(autotimertask, 1000 * 5, 60*2*1000);
	}
	//自动刷新评论列表
	public void autoReflushPostList(){
		Timer autotimer = new Timer();
		TimerTask autotimertask = new TimerTask() {
			public void run() {
				Message message = new Message();
				message.what = autoReflushQPostList;
				handler.sendMessage(message);
			}
		};
		autotimer.schedule(autotimertask, 1000 * 5, 60*1000);
	}
	//執行的asyn方法，定时刷新时执行
		class  autoReflushPostView extends AsyncTask{
						@Override
						protected void onPostExecute(Object result) {
							hideProgress(rlLoading);
							if(viewfillper.getVisibility()!=View.VISIBLE){
								invisview.requestFocus();
								invisview.setFocusable(true);
								invisview.setOnKeyListener(vfKeyListener);
							}
							if(postList.size()!=0){
							setVf();
							vf.setVisibility(View.VISIBLE);
							setPostTimerStart();
							}else{
								vf.setVisibility(View.GONE);
								reloading.setVisibility(View.VISIBLE);
								reButton.requestFocus();
								reButton.setFocusable(true);
							}
							
							super.onPostExecute(result);
						}
						//開啟定時循環展示
						@Override
						protected void onPreExecute() {
							showProgress(rlLoading);
							reloading.setVisibility(View.GONE);
							super.onPreExecute();
						}
						@Override
						protected Void doInBackground(Object... params) {
							int topicid =currenTopic.getId();
							if(topicid!=0){
							 String autopath = "http://tvsrv.webhop.net:8080/api/topics/"
									+ topicid + "/posts?page=" + topicpage + "&count=" + count;
							 System.out.println("autopath==="+autopath);
							getPostList(autopath);
							}
							return null;
						}
		}
}