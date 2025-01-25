package com.wfs.truthsearch.data;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class SearchIndexDao_Impl implements SearchIndexDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<SearchIndex> __insertAdapterOfSearchIndex;

  public SearchIndexDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfSearchIndex = new EntityInsertAdapter<SearchIndex>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `search_index` (`verseId`,`text`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final SearchIndex entity) {
        if (entity.getVerseId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getVerseId());
        }
        if (entity.getText() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getText());
        }
      }
    };
  }

  @Override
  public Object insertAll(final List<SearchIndex> searchIndexes,
      final Continuation<? super Unit> arg1) {
    if (searchIndexes == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfSearchIndex.insert(_connection, searchIndexes);
      return Unit.INSTANCE;
    }, arg1);
  }

  @Override
  public Object getSearchIndexCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM search_index";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg0);
  }

  @Override
  public Object search(final String query, final Continuation<? super List<String>> arg1) {
    final String _sql = "SELECT verseId FROM search_index WHERE text LIKE '%' || ? || '%'";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (query == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, query);
        }
        final List<String> _result = new ArrayList<String>();
        while (_stmt.step()) {
          final String _item;
          if (_stmt.isNull(0)) {
            _item = null;
          } else {
            _item = _stmt.getText(0);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg1);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
