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
public class TvTopicClientActivity implements Runnable {
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
	private TextView tv_homeline_comment;
	private ImageView tv_homeline_filmimage;
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
	public void run() {
		programList = new ArrayList<Program>();
		topicList = new ArrayList<Topic>();
		downloder = new ImageDownloder();
		programJsonArray =new JSONArray();
		initView();
		System.out.println();
		listview.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
			   if(position ==topicList.size()){
				   listview.setClickable(false);
				   topicpage=topicpage+1;
				   getTopicList(proHashMap);
				   if(topicList.size()==0){
					   Toast.makeText(mContext, "电影没有更多的话题，重新加载第一页", Toast.LENGTH_SHORT).show();
					   topicpage =1;
					   getTopicList(proHashMap);
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
		try {
			final JsonUtil ju = new JsonUtil();
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected void onPostExecute(Void result) {
					rlLoading.setVisibility(View.GONE);
					if (programJsonArray.length() == 0) {
						Toast.makeText(mContext, "该电影没有相应的话题", Toast.LENGTH_SHORT).show();
						Toast.makeText(mContext, "请按返回键退出", Toast.LENGTH_SHORT).show();
						reloading.setVisibility(View.VISIBLE);
						reButton.requestFocus();
						reButton.setFocusable(true);
						reButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								run();				
							}
						});
					} else {
						for (int i = 0; i < programJsonArray.length(); i++) {
							JSONObject jsProgram;
							try {
								jsProgram = programJsonArray.getJSONObject(i);
								JSONObject2Program jp = new JSONObject2Program();
								Program program = jp.getProgram(jsProgram);
								programList.add(program);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						programId = getProgramId(mparms);
						proHashMap = new HashMap<String, Object>();
						getTopicList( mparms);
						if(topicList.size()==0){
							Toast.makeText(mContext, "topiclist该电影没有相应的话题", Toast.LENGTH_SHORT).show();
							reloading.setVisibility(View.VISIBLE);
							reButton.requestFocus();
							reButton.setFocusable(true);
							reButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									run();				
								}
							});
						}else{
						showTopic = new StringBuffer();
						for (int i = 0; i < topicList.size(); i++) {
							String titleName = topicList.get(i).getTopic_name();
							String username = topicList.get(i).getUser()
									.getName();
							String show = username + ":" + titleName + "    ";
							showTopic.append(show);
						}
						topic_title.setVisibility(View.VISIBLE);
						topic_title.setText(showTopic);
						topic_title.setFocusable(true);
						topic_title.requestFocus();
						topic_title.setOnKeyListener(new OnKeyListener() {
							@Override
							public boolean onKey(View v, int keyCode,
									KeyEvent event) {
								if (keyCode == KeyEvent.KEYCODE_1) {
									stopTimer(rlTextTimer, rlTextTask);	
									if (topicList.size() == 0) {
										Toast.makeText(mContext,
										"没有相应的话题，请按返回键退出", Toast.LENGTH_SHORT).show();
									} else {
										System.out.println(keyCode);
										vf.removeAllViews();
										listview.setVisibility(View.VISIBLE);
										listview.addFooterView(footview);
										// changeview.setVisibility(View.VISIBLE);
										adapter =new myAdapter();
										listview.setAdapter(adapter);
										if (null !=listTimer) {
											listTimerTask.cancel();
										}
//										listTimer = new Timer();
//										 listTimerTask = new TimerTask() {
//											public void run() {
//												Message message = new Message();
//												message.what = RefalshListviewData;
//												handler.sendMessage(message);
//											}
//										};
//										listTimer.schedule(listTimerTask, 60*1000*2, 60*1000*5);
										topic_title.setVisibility(View.GONE);
										listview.requestFocus();
										boolean b = listview.requestFocus();
										System.out.println("listview" + b);
									   //TODO
										listview.setOnItemClickListener(new OnItemClickListener() {
											@Override
											public void onItemClick(
													AdapterView<?> adapterview,
													View view, int postition,
													long id) {
												vf.removeAllViews();
												vf.setVisibility(View.GONE);
												System.out.println(postition);
												postList = new ArrayList<Post>();
												if (timer != null) {
													timer.cancel();
												}
												vf.stopFlipping();
												vf.getBackground()
														.setAlpha(100);
												topic = topicList
														.get(postition);
												final int topicId = topic
														.getId();
												new AsyncTask<Void, Void, Void>() {

													@Override
													protected void onPostExecute(
															Void result) {
														for (int i = 0; i < jsonArray
																.length(); i++) {
															try {
																JSONObject jsPost = jsonArray
																		.getJSONObject(i);
																JSONObject2Post jp = new JSONObject2Post();
																Post post = jp
																		.getPost(jsPost);
																postList.add(post);
															} catch (Exception e) {
																e.printStackTrace();
															}
														}
														for (int i = 0; i < postList
																.size(); i++) {
															View initfillperview = initviewfilpper();
															initfillperview
																	.setVisibility(View.VISIBLE);
															tv_homeline_username
																	.setText(postList
																			.get(i)
																			.getUser()
																			.getName());
															tv_homeline_comment
																	.setText(postList
																			.get(i)
																			.getC());
															String userimagepath = postList
																	.get(i)
																	.getUser()
																	.getImage();
															String filmpath = postList
																	.get(i)
																	.getTopic()
																	.getProgram()
																	.getImagePath();
															try {
																Bitmap userBitmap = downloder
																		.imageDownloder(userimagepath);
																Bitmap filmBitmap = downloder
																		.imageDownloder(filmpath);
																tv_homeline_filmimage
																		.setImageBitmap(filmBitmap);
																tv_homeline_userimage
																		.setImageBitmap(userBitmap);
															} catch (Exception e) {
																e.printStackTrace();
															}
															vf.addView(initfillperview);
															vf.setVisibility(View.VISIBLE);
															isFirstLoading = false;
															//TODO
															if (null != rlvfTimer) {
																rlvfTask.cancel();
															}
															 rlvfTimer = new Timer();
															 rlvfTask = new TimerTask() {
																public void run() {
																	Message message = new Message();
																	message.what = RefalshVFData;
																	handler.sendMessage(message);
																}
															};
															rlvfTimer.schedule(rlvfTask, 60*1000*2,60*1000*5);
														}
														if (postList.size() != 0) {
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
														} else {
															Toast.makeText(
																	mContext,
																	"该话题没有评论",
																	Toast.LENGTH_SHORT).show();
														}
														super.onPostExecute(result);
													}

													@Override
													protected Void doInBackground(
															Void... params) {
														String path = "http://tvsrv.webhop.net:8080/api/topics/"
																+ topicId
																+ "/posts?page="+page+"&count="+count;
														JsonUtil ju = new JsonUtil();
														try {
															jsonArray = ju
																	.getSource(path);
														} catch (Exception e) {
															e.printStackTrace();
														}
														return null;
													}

												}.execute();
											}
										});
										listview.setVisibility(View.VISIBLE);
									}
								} 
								else if(keyCode==KeyEvent.KEYCODE_BACK){
									topic_title.setVisibility(View.GONE);
									return false;
								}
								return false;
							}
						});
						listview.setOnKeyListener(new OnKeyListener() {
							@Override
							public boolean onKey(View view, int i,
									KeyEvent keyevent) {
								if (i == KeyEvent.KEYCODE_BACK) {
									isFirstBack=false;
									listview.setVisibility(View.GONE);
									vf.setVisibility(View.VISIBLE);
									stopTimer(listTimer, listTimerTask);
									invisview.requestFocus();
									invisview.setFocusableInTouchMode(true);
									invisview.setFocusable(true);
									boolean v = invisview.requestFocus();
									System.out
											.println("listview消失以后 invisview====》》"
													+ v);
									return true;
								} else if (i == keyevent.KEYCODE_2) {
									stopTimer(listTimer, listTimerTask);
									listview.setVisibility(View.GONE);
									vf.setVisibility(View.GONE);
									topic_title.setText(showTopic);
									topic_title.setVisibility(View.VISIBLE);
									topic_title.requestFocus();
									topic_title.setFocusable(true);
									boolean b = topic_title.requestFocus();
									if (null != rlTextTimer) {
										rlTextTask.cancel();
									}
									 rlTextTimer = new Timer();
									 rlTextTask = new TimerTask() {
										public void run() {
											Message message = new Message();
											message.what = RefalshTextData;
											handler.sendMessage(message);
										}
									};
									
									rlTextTimer.schedule(rlTextTask, 60*1000*2, 60*1000*5);
								}
								return false;
							}
						});
						invisview.setOnKeyListener(new OnKeyListener() {
							@Override
							public boolean onKey(View view, int i,
									KeyEvent keyevent) {
								
								 
								if (i == KeyEvent.KEYCODE_1) {
									listview.setVisibility(View.VISIBLE);
									if (null !=listTimer) {
										listTimerTask.cancel();
									}
									listTimer = new Timer();
									 listTimerTask = new TimerTask() {
										public void run() {
											Message message = new Message();
											message.what = RefalshListviewData;
											handler.sendMessage(message);
										}
									};
									listTimer.schedule(listTimerTask, 60*1000*2, 60*1000*5);
								} else if (i == keyevent.KEYCODE_2) {
									listview.setVisibility(View.GONE);
									vf.setVisibility(View.GONE);
									stopTimer(listTimer, listTimerTask);
									topic_title.setText(showTopic);
									topic_title.setVisibility(View.VISIBLE);
									topic_title.requestFocus();
									topic_title.setFocusable(true);
								}
//								else if(i==keyevent.KEYCODE_BACK){
//									if(!isFirstBack){
//									stopTimer(timer, imagetask);
//									stopTimer(rlvfTimer, rlvfTask);
//									System.out.println("我已经被捕捉到了");
//									}
//								}
								return false;
							}
						});
						 rlTextTimer = new Timer();
						 rlTextTask = new TimerTask() {
							public void run() {
								Message message = new Message();
								message.what = RefalshTextData;
								handler.sendMessage(message);
							}
						};
						
						rlTextTimer.schedule(rlTextTask, 60*1000*2, 60*1000*5);
					}
					}
					super.onPostExecute(result);
				}
				@Override
				protected void onPreExecute() {
					rlLoading.setVisibility(View.VISIBLE);
					super.onPreExecute();
				}
				@Override
				protected Void doInBackground(Void... params) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						programJsonArray = ju.getSource(path);
						if(programJsonArray.length()==0){
							Toast.makeText(mContext, "网络错误，获取数据失败", Toast.LENGTH_SHORT).show();
							//TODO
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != rlTextTimer) {
			rlTextTask.cancel();
		}
	}
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
	void initView() {
		
		topiclistview = View.inflate(mContext, R.layout.test, null);
		vf = (ViewFlipper) topiclistview.findViewById(R.id.topic_viewflipper);
		txt_loading = (TextView) topiclistview.findViewById(R.id.txt_loading);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
		mContainer.removeAllViews();
		mContainer.addView(topiclistview, lp);
		listview = (ListView) topiclistview.findViewById(R.id.subjectlist);
		listview.getBackground().setAlpha(50);
		invisview = (Button) topiclistview.findViewById(R.id.invisview);
		topiclistview.setFocusable(true);
		topiclistview.requestFocus();
		topic_title = (TextView) topiclistview.findViewById(R.id.topic_title);
		topic_title.setVisibility(View.GONE);
		rlLoading = (RelativeLayout) topiclistview.findViewById(R.id.loading);
		rlLoading.setVisibility(View.VISIBLE);
		reloading=(RelativeLayout) topiclistview.findViewById(R.id.reloading);
		reButton =(Button) topiclistview.findViewById(R.id.txt_reloading);
		LayoutInflater inflate =LayoutInflater.from(mContext);
		 footview = inflate.inflate(R.layout.footview, null);
	}
	public void getTopicList(final HashMap<String, Object> objects) {
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

	public void init(Context context, ViewGroup container,HashMap<String,Object> config) {
		mContext = context;
		mContainer = container;
		mparms =config;
		
		Log.d("tv_topic", "config is:" + config);
		Log.d("tv_topic", "config.Pragram is:" + config.get("PROGRAM_NAME"));
		Log.d("tv_topic", "init has finished");
		
	}

	public class myAdapter extends BaseAdapter {
		protected int mycount= topicList.size();
		@Override
		public int getCount() {
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
				convertView.getBackground().setAlpha(100);
				holder.topic_image = (ImageView) convertView
						.findViewById(R.id.topic_image);
				holder.topic_username = (TextView) convertView
						.findViewById(R.id.topic_username);
				holder.topic_title = (TextView) convertView
						.findViewById(R.id.topic_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.topic_username.setText(topicList.get(position).getUser()
					.getName());
			holder.topic_title.setText(topicList.get(position).getTopic_name());
			try {
				Bitmap bitmap = downloder.imageDownloder(topicList
						.get(position).getUser().getImage());
				holder.topic_image.setImageBitmap(bitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	public final class ViewHolder {
		public ImageView topic_image;
		public TextView topic_username;
		public TextView topic_title;
		public ImageView tv_homeline_filmimage;
		public TextView tv_homeline_filmname;
		public TextView tv_homeline_title;
	}
	View initviewfilpper() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View viewfilpperitem = inflater.inflate(R.layout.mytopic_2, null);
		tv_homeline_userimage = (ImageView) viewfilpperitem
				.findViewById(R.id.tv_homeline_userimage);
		// tv_homeline_userimage.setAlpha(100);
		tv_homeline_username = (TextView) viewfilpperitem
				.findViewById(R.id.tv_homeline_username);
		tv_homeline_username.getBackground().setAlpha(100);
		tv_homeline_comment = (TextView) viewfilpperitem
				.findViewById(R.id.tv_homeline_comment);
		tv_homeline_comment.getBackground().setAlpha(100);
		tv_homeline_filmimage = (ImageView) viewfilpperitem
				.findViewById(R.id.tv_homeline_filmimage);
		tv_homeline_filmimage.getBackground().setAlpha(100);
		return viewfilpperitem;
	}
//TODO 
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ChangeImage:
				setImageAnimation(vf, postList);
				break;
			case RefalshTextData:
				loadMoreTextView();
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


//TODO
	public void loadMoreTextView() {
		topicList = new ArrayList<Topic>();
		page =page+1;
		int titlevisiable=topic_title.getVisibility();
		boolean b = (titlevisiable==visible);
		System.out.println("是否为可见"+b);
		if (titlevisiable == visible) {
			topic_title.setVisibility(View.GONE);
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected void onPostExecute(Void result) {
					    if(topicList.size()==0){
					    	page=0;
					    	topic_title.setVisibility(View.VISIBLE);
					    	loadMoreTextView();
					    }else{
					    	isTextProcessbar=false;
						showTopic = new StringBuffer();
						for (int i = 0; i < topicList.size(); i++) {
							String titleName = topicList.get(i).getTopic_name();
							String username = topicList.get(i).getUser()
									.getName();
							String show = username + ":" + titleName + "    ";
							showTopic.append(show);
						}
						topic_title.setVisibility(View.VISIBLE);
						topic_title.setText(showTopic);
						topic_title.setFocusable(true);
						topic_title.requestFocus();
					    }
						super.onPostExecute(result);
				}
				@Override
				protected void onPreExecute() {
					if(!isTextProcessbar){
					txt_loading.setText("正在加载最新数据");
					isTextProcessbar =true;
					}
					super.onPreExecute();
				}

				@Override
				protected Void doInBackground(Void... params) {
					
					jishiqi++;
					System.out.println("textview asyn执行了"+jishiqi+"次");
					JsonUtil ju = new JsonUtil();
					try {
							for (int i = 0; i < programJsonArray.length(); i++) {
								JSONObject jsProgram;
								try {
									jsProgram = programJsonArray.getJSONObject(i);
									JSONObject2Program jp = new JSONObject2Program();
									Program program = jp.getProgram(jsProgram);
									programList.add(program);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							proHashMap = new HashMap<String, Object>();
							proHashMap.put("PROGRAM_NAME", "武林外传");
							getTopicList(proHashMap);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}.execute();
		}else{
			stopTimer(rlTextTimer, rlTextTask);
		}
	}
//TODO
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
						View initfillperview = initviewfilpper();
						initfillperview.setVisibility(View.VISIBLE);
						tv_homeline_username.setText(postList.get(i).getUser()
								.getName());
						tv_homeline_comment.setText(postList.get(i).getC());
						String userimagepath = postList.get(i).getUser()
								.getImage();
						String filmpath = postList.get(i).getTopic()
								.getProgram().getImagePath();
						try {
							Bitmap userBitmap = downloder
									.imageDownloder(userimagepath);
							Bitmap filmBitmap = downloder
									.imageDownloder(filmpath);
							tv_homeline_filmimage.setImageBitmap(filmBitmap);
							tv_homeline_userimage.setImageBitmap(userBitmap);
						} catch (Exception e) {
							e.printStackTrace();
						}
						vf.addView(initfillperview);
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
}