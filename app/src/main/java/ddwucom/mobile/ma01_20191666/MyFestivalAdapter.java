    package ddwucom.mobile.ma01_20191666;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyFestivalAdapter extends BaseAdapter{
    public static final String TAG = "MyFestivalAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<FestivalDTO> list;

    public MyFestivalAdapter(Context context, int resource, ArrayList<FestivalDTO> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public FestivalDTO getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = view.findViewById(R.id.dbFestivalName);
            viewHolder.tvOpar = view.findViewById(R.id.dbFestivalLoc);
            viewHolder.tvDate = view.findViewById(R.id.dbFestivalDate);
            viewHolder.tvMemo = view.findViewById(R.id.dbFestivalMemo);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        FestivalDTO dto = list.get(position);
        viewHolder.tvName.setText(dto.getName());
        viewHolder.tvOpar.setText(dto.getLoc());
        viewHolder.tvDate.setText(dto.getStartDate() + " ~ " + dto.getEndDate());
        viewHolder.tvMemo.setText(dto.getMemo());

        Log.d(TAG, "getMemo : " + dto.getMemo());

        return view;
    }

    public void setList(ArrayList<FestivalDTO> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView tvName = null;
        public TextView tvOpar = null;
        public TextView tvDate = null;
        public TextView tvMemo = null;
    }
}
