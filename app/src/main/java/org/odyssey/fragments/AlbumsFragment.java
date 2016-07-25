package org.odyssey.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.odyssey.R;
import org.odyssey.listener.OnArtistSelectedListener;
import org.odyssey.loaders.AlbumLoader;
import org.odyssey.models.AlbumModel;
import org.odyssey.utils.MusicLibraryHelper;

import java.util.List;

public class AlbumsFragment extends GenericAlbumsFragment {

    /**
     * Listener to open an artist
     */
    private OnArtistSelectedListener mArtistSelectedCallback;

    /**
     * Called to create instantiate the UI of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Called when the fragment is first attached to its context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mArtistSelectedCallback = (OnArtistSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArtistSelectedListener");
        }
    }

    /**
     * This method creates a new loader for this fragment.
     * @param id The id of the loader
     * @param bundle Optional arguments
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<AlbumModel>> onCreateLoader(int id, Bundle bundle) {
        // all albums
        return new AlbumLoader(getActivity(), -1);
    }

    /**
     * Create the context menu.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_albums_fragment, menu);
    }

    /**
     * Hook called when an menu item in the context menu is selected.
     * @param item The menu item that was selected.
     * @return True if the hook was consumed here.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (info == null) {
            return super.onContextItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.fragment_albums_action_enqueue:
                enqueueAlbum(info.position);
                return true;
            case R.id.fragment_albums_action_play:
                playAlbum(info.position);
                return true;
            case R.id.fragment_albums_action_showartist:
                showArtist(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Open a fragment for the artist of the selected album.
     * @param position the position of the selected album in the adapter
     */
    private void showArtist(int position) {
        // identify current artist

        AlbumModel clickedAlbum = (AlbumModel) mAlbumsGridViewAdapter.getItem(position);

        String artistTitle = clickedAlbum.getArtistName();
        long artistID = MusicLibraryHelper.getArtistIDFromName(artistTitle, getActivity());

        // Send the event to the host activity
        mArtistSelectedCallback.onArtistSelected(artistTitle, artistID);
    }
}
