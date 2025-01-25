package com.wfs.truthsearch.data;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class BibleDatabase_Impl extends BibleDatabase {
  private volatile SearchIndexDao _searchIndexDao;

  private volatile FullVerseDao _fullVerseDao;

  private volatile SearchIndexEsvDao _searchIndexEsvDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "dbce777b765d3f43b54aea1146aa26b5", "f7e349727aba8ac50e8580dee2abcada") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `search_index` (`verseId` TEXT NOT NULL, `text` TEXT NOT NULL, PRIMARY KEY(`verseId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `full_verse` (`verseId` TEXT NOT NULL, `text` TEXT NOT NULL, PRIMARY KEY(`verseId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `search_index_esv` (`verseId` TEXT NOT NULL, `text` TEXT NOT NULL, PRIMARY KEY(`verseId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'dbce777b765d3f43b54aea1146aa26b5')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `search_index`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `full_verse`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `search_index_esv`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsSearchIndex = new HashMap<String, TableInfo.Column>(2);
        _columnsSearchIndex.put("verseId", new TableInfo.Column("verseId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSearchIndex.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysSearchIndex = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesSearchIndex = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSearchIndex = new TableInfo("search_index", _columnsSearchIndex, _foreignKeysSearchIndex, _indicesSearchIndex);
        final TableInfo _existingSearchIndex = TableInfo.read(connection, "search_index");
        if (!_infoSearchIndex.equals(_existingSearchIndex)) {
          return new RoomOpenDelegate.ValidationResult(false, "search_index(com.wfs.truthsearch.data.SearchIndex).\n"
                  + " Expected:\n" + _infoSearchIndex + "\n"
                  + " Found:\n" + _existingSearchIndex);
        }
        final Map<String, TableInfo.Column> _columnsFullVerse = new HashMap<String, TableInfo.Column>(2);
        _columnsFullVerse.put("verseId", new TableInfo.Column("verseId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFullVerse.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysFullVerse = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesFullVerse = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFullVerse = new TableInfo("full_verse", _columnsFullVerse, _foreignKeysFullVerse, _indicesFullVerse);
        final TableInfo _existingFullVerse = TableInfo.read(connection, "full_verse");
        if (!_infoFullVerse.equals(_existingFullVerse)) {
          return new RoomOpenDelegate.ValidationResult(false, "full_verse(com.wfs.truthsearch.data.FullVerse).\n"
                  + " Expected:\n" + _infoFullVerse + "\n"
                  + " Found:\n" + _existingFullVerse);
        }
        final Map<String, TableInfo.Column> _columnsSearchIndexEsv = new HashMap<String, TableInfo.Column>(2);
        _columnsSearchIndexEsv.put("verseId", new TableInfo.Column("verseId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSearchIndexEsv.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysSearchIndexEsv = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesSearchIndexEsv = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSearchIndexEsv = new TableInfo("search_index_esv", _columnsSearchIndexEsv, _foreignKeysSearchIndexEsv, _indicesSearchIndexEsv);
        final TableInfo _existingSearchIndexEsv = TableInfo.read(connection, "search_index_esv");
        if (!_infoSearchIndexEsv.equals(_existingSearchIndexEsv)) {
          return new RoomOpenDelegate.ValidationResult(false, "search_index_esv(com.wfs.truthsearch.data.SearchIndexEsv).\n"
                  + " Expected:\n" + _infoSearchIndexEsv + "\n"
                  + " Found:\n" + _existingSearchIndexEsv);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "search_index", "full_verse", "search_index_esv");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "search_index", "full_verse", "search_index_esv");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SearchIndexDao.class, SearchIndexDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FullVerseDao.class, FullVerseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SearchIndexEsvDao.class, SearchIndexEsvDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SearchIndexDao searchIndexDao() {
    if (_searchIndexDao != null) {
      return _searchIndexDao;
    } else {
      synchronized(this) {
        if(_searchIndexDao == null) {
          _searchIndexDao = new SearchIndexDao_Impl(this);
        }
        return _searchIndexDao;
      }
    }
  }

  @Override
  public FullVerseDao fullVerseDao() {
    if (_fullVerseDao != null) {
      return _fullVerseDao;
    } else {
      synchronized(this) {
        if(_fullVerseDao == null) {
          _fullVerseDao = new FullVerseDao_Impl(this);
        }
        return _fullVerseDao;
      }
    }
  }

  @Override
  public SearchIndexEsvDao searchIndexEsvDao() {
    if (_searchIndexEsvDao != null) {
      return _searchIndexEsvDao;
    } else {
      synchronized(this) {
        if(_searchIndexEsvDao == null) {
          _searchIndexEsvDao = new SearchIndexEsvDao_Impl(this);
        }
        return _searchIndexEsvDao;
      }
    }
  }
}
