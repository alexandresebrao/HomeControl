package project.alexandre.homecontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alexandre on 2015-03-29.
 */
public class MicroWaveAdapter extends ArrayAdapter<MicroWaveItem> {
    private ArrayList<MicroWaveItem> microwavelist;

    private MicroWaveAdapterCallback callback;

    public MicroWaveAdapter(Context context, int textViewResourceId, ArrayList<MicroWaveItem> microwavelist) {
        super(context, textViewResourceId, microwavelist);
        this.microwavelist = microwavelist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listmicrowave, null);
        }

        final MicroWaveItem item = microwavelist.get(position);
        if (item != null) {
            TextView identity = (TextView) v.findViewById(R.id.microwaveidentity);
            Button btnMicroWaveDelete = (Button) v.findViewById(R.id.btnDeleteMicroWave);
            Button btnMicroWaveStart = (Button) v.findViewById(R.id.btnStartMicrowave);

            ImageView imgMicrowave = (ImageView) v.findViewById(R.id.imgMicroWaverIcon);

            if (identity != null) {
                identity.setText(item.identity);
            }

            if (btnMicroWaveDelete != null){
                btnMicroWaveDelete.setTag(item.id);
            }

            if ((imgMicrowave != null) && item.inUse){
                imgMicrowave.setImageResource(R.drawable.ic_microwave_red);
            }
            if ((imgMicrowave != null) && !item.inUse){
                imgMicrowave.setImageResource(R.drawable.ic_microwave_green);
            }

            btnMicroWaveDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (callback != null) {

                        callback.deleteMicroWavePressed(item.id);
                    }
                }

            });

            btnMicroWaveStart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!item.inUse) {
                        if (callback != null) {

                            callback.startMicroWavePressed(item.id);
                        }

                    } else {
                        Toast.makeText(getContext(), "Machine in use", Toast.LENGTH_SHORT).show();
                    }

                }

            });
        }


        return v;
    }

    public void setCallback(MicroWaveAdapterCallback callback){

        this.callback = callback;
    }

    public interface MicroWaveAdapterCallback {

        public void deleteMicroWavePressed(String position);
        public void startMicroWavePressed(String id);
    }
}
