package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentMemberListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentMemberListCardBinding;

public class MemberListRecyclerViewAdapter extends RecyclerView.Adapter<MemberListRecyclerViewAdapter.MemberListViewHolder> {

    private final List<String> mMembers;

    public MemberListRecyclerViewAdapter(@NonNull List<String> members) {
        mMembers = members;
    }

    @NonNull
    @Override
    public MemberListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_member_list_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberListRecyclerViewAdapter.MemberListViewHolder holder, int position) {
        holder.setMember(mMembers.get(position));
    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class MemberListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentMemberListCardBinding binding;

        public MemberListViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentMemberListCardBinding.bind(view);
        }
        void setMember(final String member) {
            binding.textTitle.setText(member);
        }
    }
}
