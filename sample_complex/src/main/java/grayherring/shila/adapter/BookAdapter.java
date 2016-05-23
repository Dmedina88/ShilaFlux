package grayherring.shila.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import grayherring.shila.R;
import grayherring.shila.activity.BaseActivity;
import grayherring.shila.activity.DetailsActivity;
import grayherring.shila.flux.SwagActionCreator;
import grayherring.shila.model.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 12/3/2015.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

  Context context;
  List<Book> data;
  SwagActionCreator actionCenter = SwagActionCreator.getInstance();
  private int lastPosition = -1;

  public BookAdapter(List<Book> books, Context context) {
    data = new ArrayList<>();
    if (books != null) {
      data.addAll(books);
    }
    this.context = context;
  }

  public void changeData(List<Book> books) {
    data.clear();
    data.addAll(books);
    this.notifyDataSetChanged();
  }

  public void addBook(Book book) {
    data.add(book);
    this.notifyDataSetChanged();
  }

  public void updateBook(int index, Book book) {
    data.set(index, book);
    notifyItemChanged(index);
  }

  public void deleteBook(int index) {
    data.remove(index);
    notifyItemRemoved(index);
  }

  public void deleteAllBooks() {
    data.clear();
    notifyDataSetChanged();
  }

  @Override
  public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
    return new BookViewHolder(v);
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  @Override
  public void onBindViewHolder(BookViewHolder holder, int position) {

    holder.title.setText(data.get(position).getTitle());
    holder.author.setText(data.get(position).getAuthor());
    setAnimation(holder.container, position);
  }

  private void setAnimation(View viewToAnimate, int position) {
    if (position > lastPosition) {
      Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
      viewToAnimate.startAnimation(animation);
      lastPosition = position;
    }
  }

  class BookViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.book_title)
    TextView title;
    @BindView(R.id.book_authors)
    TextView author;
    @BindView(R.id.book_item)
    LinearLayout container;

    public BookViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.book_item)
    public void ItemClicked(View view) {
      actionCenter.selectBook(data.get(getAdapterPosition()), getAdapterPosition(),
          BookAdapter.this.getClass().getSimpleName() + this.hashCode()
              + BookAdapter.this.hashCode());

      Intent i = new Intent(context, DetailsActivity.class);
      actionCenter.startActivity(i, ((BaseActivity) context).sourceId());
    }
  }
}
