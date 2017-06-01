package com.fekracomputers.islamiclibrary.tableOFContents.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.fekracomputers.islamiclibrary.R;
import com.fekracomputers.islamiclibrary.browsing.fragment.SortListDialogFragment;
import com.fekracomputers.islamiclibrary.databases.UserDataDBHelper;
import com.fekracomputers.islamiclibrary.model.BookPartsInfo;
import com.fekracomputers.islamiclibrary.model.Highlight;
import com.fekracomputers.islamiclibrary.tableOFContents.adapter.HighlightRecyclerViewAdapter;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link onHighlightClickListener}
 * interface.
 */
public class HighlightFragment extends Fragment implements SortListDialogFragment.OnSortDialogListener {

    private int bookId;
    private onHighlightClickListener mListener;
    private HighlightRecyclerViewAdapter highlightRecyclerViewAdapter;
    private boolean sortedByPage = true;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HighlightFragment() {
    }

    public static HighlightFragment newInstance(int bookId) {
        HighlightFragment fragment = new HighlightFragment();
        Bundle args = new Bundle();
        args.putInt("bookId", bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_highlight, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                SortListDialogFragment sortListDialogFragment = SortListDialogFragment.newInstance(R.array.highlight_list_sorting, highlightRecyclerViewAdapter.getCurrentSortIndex());
                //see this answer http://stackoverflow.com/a/37794319/3061221
                FragmentManager fm = getChildFragmentManager();
                sortListDialogFragment.show(fm, "fragment_sort");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void sortMethodSelected(int which) {
        highlightRecyclerViewAdapter.sortBy(which);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundl = getArguments();
        if (bundl != null) {
            bookId = bundl.getInt("bookId");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_highlight_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ViewStub zeroView = (ViewStub) view.findViewById(R.id.zero_highlights);

        UserDataDBHelper userDataDBHelper = UserDataDBHelper.getInstance(getContext(), bookId);
        ArrayList<Highlight> userDataDBHelperAllHighlights = userDataDBHelper.getAllHighlights();

        if (userDataDBHelperAllHighlights.size() != 0) {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            highlightRecyclerViewAdapter = new HighlightRecyclerViewAdapter(
                    userDataDBHelperAllHighlights,
                    mListener,
                    getContext().getApplicationContext(),
                    getActivity().getPreferences(Context.MODE_PRIVATE));
            highlightRecyclerViewAdapter.setHasStableIds(true);
            recyclerView.setAdapter(highlightRecyclerViewAdapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            zeroView.setVisibility(View.VISIBLE);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onHighlightClickListener) {
            mListener = (onHighlightClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onHighlightClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onHighlightClickListener {
        void onHighlightClicked(Highlight highlight);

        BookPartsInfo getBookPartsInfo();
    }
}
