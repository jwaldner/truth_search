package com.wfs.truthsearch.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
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
@SuppressWarnings({"unchecked", "deprecation"})
public final class BibleDatabase_Impl extends BibleDatabase {
  private volatile SearchIndexDao _searchIndexDao;

  private volatile FullVerseDao _fullVerseDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `search_index` (`verseId` TEXT NOT NULL, `text` TEXT NOT NULL, PRIMARY KEY(`verseId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `full_verse` (`verseId` TEXT NOT NULL, `text` TEXT NOT NULL, PRIMARY KEY(`verseId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'cda9998933abebebba14fe6685dfb8de')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `search_index`");
        db.execSQL("DROP TABLE IF EXISTS `full_verse`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSearchIndex = new HashMap<String, TableInfo.Column>(2);
        _columnsSearchIndex.put("verseId", new TableInfo.Column("verseId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSearchIndex.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSearchIndex = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSearchIndex = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSearchIndex = new TableInfo("search_index", _columnsSearchIndex, _foreignKeysSearchIndex, _indicesSearchIndex);
        final TableInfo _existingSearchIndex = TableInfo.read(db, "search_index");
        if (!_infoSearchIndex.equals(_existingSearchIndex)) {
          return new RoomOpenHelper.ValidationResult(false, "search_index(com.wfs.truthsearch.data.SearchIndex).\n"
                  + " Expected:\n" + _infoSearchIndex + "\n"
                  + " Found:\n" + _existingSearchIndex);
        }
        final HashMap<String, TableInfo.Column> _columnsFullVerse = new HashMap<String, TableInfo.Column>(2);
        _columnsFullVerse.put("verseId", new TableInfo.Column("verseId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFullVerse.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFullVerse = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFullVerse = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFullVerse = new TableInfo("full_verse", _columnsFullVerse, _foreignKeysFullVerse, _indicesFullVerse);
        final TableInfo _existingFullVerse = TableInfo.read(db, "full_verse");
        if (!_infoFullVerse.equals(_existingFullVerse)) {
          return new RoomOpenHelper.ValidationResult(false, "full_verse(com.wfs.truthsearch.data.FullVerse).\n"
                  + " Expected:\n" + _infoFullVerse + "\n"
                  + " Found:\n" + _existingFullVerse);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "cda9998933abebebba14fe6685dfb8de", "132a809ce8fd9ac448686bca03ae2204");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "search_index","full_verse");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `search_index`");
      _db.execSQL("DELETE FROM `full_verse`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SearchIndexDao.class, SearchIndexDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FullVerseDao.class, FullVerseDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
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
}
