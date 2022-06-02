package edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentRecentMessageBinding;


public class MessageRecylerViewAdapter extends
        RecyclerView.Adapter<MessageRecylerViewAdapter.MessageViewHolder> {

    //Store all of the Message to present
    private final List<HashMap<String,String>> mList;

    public DateUtils dateutils = new DateUtils();

    public MessageRecylerViewAdapter(List<HashMap<String, String>> items) {
        this.mList = items;
    }



    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Edit onCreateViewHolder() to inflate the layout fragment_blog_card:
        return new MessageViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_recent_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecylerViewAdapter.MessageViewHolder holder, int position) {
        holder.setMessage(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Message Recycler View.
     */
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentRecentMessageBinding binding;

        public MessageViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentRecentMessageBinding.bind(view);
        }

        void setMessage(final HashMap<String,String> m) {
            Log.d("test", "setMessage: " + m.get("time"));
            binding.RecentChatName.setText((m.get("chatName")));
            String message = m.get("name") +": " + m.get("message");
            if (message.length() > 95) {
                message = message.substring(0,92) + "...";
            }
            binding.RecentText.setText(message);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("PST"));
            try {
                long time = sdf.parse( m.get("time")).getTime();
                long now = System.currentTimeMillis();
                CharSequence ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                binding.time.setText(ago);
                Log.d("test", "setMessage::::::::: " + ago);
            } catch (ParseException e) {
                e.printStackTrace();
            }



//            try {
//                final String NEW_FORMAT = "mm/dd/yyyy";
//                final String OLD_FORMAT = "yyyy-mm-dd";
//                String oldDateString = m.get("time").split("T")[0];
//                String newDateString;
//
//                SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
//                Date d = sdf.parse(oldDateString);
//                sdf.applyPattern(NEW_FORMAT);
//                newDateString = sdf.format(d);
//                binding.time.setText(newDateString);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


            //binding.time.setText(m.get("time"));

            //calendar.setTime(dateData.get2DigitYearStart());
            //int day = calendar.get(Calendar.DAY_OF_WEEK);


            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeLandingFragmentDirections.ActionNavigationHomeToNavigationChat direction =
                            HomeLandingFragmentDirections.actionNavigationHomeToNavigationChat(m.get("chatName"));
                    direction.setRoom((Integer.valueOf(m.get("chatId"))));
                    Navigation.findNavController(view).navigate(direction);
                }
            });
        }
    }

}
