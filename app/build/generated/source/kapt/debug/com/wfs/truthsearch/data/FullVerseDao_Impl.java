package com.wfs.truthsearch.data;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class FullVerseDao_Impl implements FullVerseDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<FullVerse> __insertAdapterOfFullVerse;

  public FullVerseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfFullVerse = new EntityInsertAdapter<FullVerse>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `full_verse` (`verseId`,`text`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final FullVerse entity) {
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
  public Object insertAll(final List<FullVerse> fullVerses,
      final Continuation<? super Unit> $completion) {
    if (fullVerses == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfFullVerse.insert(_connection, fullVerses);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object getFullVerseCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM full_verse";
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
    }, $completion);
  }

  @Override
  public Object getVerse(final String verseId, final Continuation<? super String> $completion) {
    final String _sql = "SELECT text FROM full_verse WHERE verseId = ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (verseId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, verseId);
        }
        final String _result;
        if (_stmt.step()) {
          if (_stmt.isNull(0)) {
            _result = null;
          } else {
            _result = _stmt.getText(0);
          }
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getFullVerses(final List<String> verseIds,
      final Continuation<? super List<FullVerse>> $completion) {
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT * FROM full_verse WHERE verseId IN (");
    final int _inputSize = verseIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        for (String _item : verseIds) {
          if (_item == null) {
            _stmt.bindNull(_argIndex);
          } else {
            _stmt.bindText(_argIndex, _item);
          }
          _argIndex++;
        }
        final int _columnIndexOfVerseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verseId");
        final int _columnIndexOfText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "text");
        final List<FullVerse> _result = new ArrayList<FullVerse>();
        while (_stmt.step()) {
          final FullVerse _item_1;
          final String _tmpVerseId;
          if (_stmt.isNull(_columnIndexOfVerseId)) {
            _tmpVerseId = null;
          } else {
            _tmpVerseId = _stmt.getText(_columnIndexOfVerseId);
          }
          final String _tmpText;
          if (_stmt.isNull(_columnIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _stmt.getText(_columnIndexOfText);
          }
          _item_1 = new FullVerse(_tmpVerseId,_tmpText);
          _result.add(_item_1);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
