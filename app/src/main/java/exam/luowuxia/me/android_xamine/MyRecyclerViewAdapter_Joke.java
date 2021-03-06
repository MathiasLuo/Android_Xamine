package exam.luowuxia.me.android_xamine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import Bean.JokeBean;

/**
 * Created by Administrator on 2015/5/30 0030.
 */
public class MyRecyclerViewAdapter_Joke extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<JokeBean> datas = null;
    private OnItemClickListener mListener;
    private int currount;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public MyRecyclerViewAdapter_Joke(List<JokeBean> datas, int currount) {
        this.currount = currount;
        this.datas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recy_item, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footerview, parent, false);
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final int aa = position;
        if (holder instanceof MyViewHolder) {
            holder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.OnItemClick(v, datas.get(aa));
                        }
                    }
            );

            ((MyViewHolder) holder).tv_time.setText(datas.get(position).getTime());
            ((MyViewHolder) holder).tv_tucao.setText("吐槽：" + datas.get(position).getTucao());
            ((MyViewHolder) holder).tv_title.setText(datas.get(position).getTitle());
            ((MyViewHolder) holder).tv_dianzhan.setText(datas.get(position).getDianzhan());
            ((MyViewHolder) holder).tv_chaping.setText(datas.get(position).getChaping());

            ((MyViewHolder) holder).item_text_joke.setText("     " + datas.get(position).getText_content());

        }


    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    /**
     * 批量增加
     */
    public void addItems(List<JokeBean> items) {
        if (items == null)
            return;
        this.datas.addAll(0, items);
        this.notifyItemRangeInserted(0, items.size());
    }

    public interface OnItemClickListener {
        public void OnItemClick(View view, JokeBean data);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tv_title, tv_time, tv_dianzhan, tv_chaping, tv_tucao, item_text_joke;
        private ImageView imagView_shezi;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_title = (TextView) itemView.findViewById(R.id.item_title_joke);
            tv_chaping = (TextView) itemView.findViewById(R.id.item_tv_chaping_joke);
            tv_dianzhan = (TextView) itemView.findViewById(R.id.item_tv_dianzhan_joke);
            tv_time = (TextView) itemView.findViewById(R.id.item_time_joke);
            tv_tucao = (TextView) itemView.findViewById(R.id.item_tv_tucao_joke);
            item_text_joke = (TextView) itemView.findViewById(R.id.item_text_joke);
            imagView_shezi = (ImageView) itemView.findViewById(R.id.item_imageview_shezi_joke);
        }

    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);

            TextView tv = (TextView) view.findViewById(R.id.tt);
            tv.setText("当前第 " + currount + " 页" + "\n正在加载下一页......");
        }

    }
}

