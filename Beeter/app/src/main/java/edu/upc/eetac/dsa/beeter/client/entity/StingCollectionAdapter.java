package edu.upc.eetac.dsa.beeter.client.entity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.content.Context;

import edu.upc.eetac.dsa.beeter.R;

/**
 * Created by Marta_ on 29/12/2015.
 */
public class StingCollectionAdapter extends BaseAdapter {
    private StingCollection stings;
    private LayoutInflater layoutInflater;
    private final static String TAG =StingCollectionAdapter.class.toString();

    public StingCollectionAdapter(Context context, StingCollection stingCollection){
        layoutInflater = LayoutInflater.from(context);
        this.stings = stingCollection;
    }

    class ViewHolder{
        TextView textViewAutor;
        TextView textViewSubject;
        //TextView textViewDate;

        ViewHolder(View row){
            this.textViewAutor = (TextView) row
                    .findViewById(R.id.textViewAutor);
            this.textViewSubject = (TextView) row
                    .findViewById(R.id.textViewAsunto);
            //this.textViewDate = (TextView) row.findViewById(R.id.textViewDate);
        }
    }
    @Override
    public int getCount() {
        return stings.getStings().size();
    }

    @Override
    public Object getItem(int position) {
        return stings.getStings().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "entro en el getView");
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_elemento_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //String Autor = strings.getStrings().get(position).getMainText();
        String Autor = stings.getStings().get(position).getCreator();
        Log.d(TAG, Autor);
        String Subject = stings.getStings().get(position).getSubject();
        //String Date = hola.getCreationTimestamp().toString();
        //String creation_time = SimpleDateFormat.getInstance().format(data.get(position).getCreationTimestamp());
        Log.d(TAG, Subject);

        viewHolder.textViewAutor.setText(Autor);
        viewHolder.textViewSubject.setText(Subject);
        //viewHolder.textViewDate.setText(Date);
        return convertView;
    }
}
