package com.taskflow.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.taskflow.app.data.local.entity.TaskEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TaskEntity> __insertionAdapterOfTaskEntity;

  private final EntityDeletionOrUpdateAdapter<TaskEntity> __deletionAdapterOfTaskEntity;

  private final EntityDeletionOrUpdateAdapter<TaskEntity> __updateAdapterOfTaskEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTaskById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteChildrenByParentId;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTaskStatus;

  private final SharedSQLiteStatement __preparedStmtOfIncrementPomodoroCount;

  public TaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTaskEntity = new EntityInsertionAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tasks` (`id`,`title`,`description`,`status`,`priority`,`dueDate`,`parentId`,`order`,`aiGenerated`,`syncStatus`,`pomodoroCount`,`createTime`,`updateTime`,`extra1`,`extra2`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindString(4, entity.getStatus());
        statement.bindLong(5, entity.getPriority());
        if (entity.getDueDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDueDate());
        }
        if (entity.getParentId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getParentId());
        }
        statement.bindLong(8, entity.getOrder());
        final int _tmp = entity.getAiGenerated() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindString(10, entity.getSyncStatus());
        statement.bindLong(11, entity.getPomodoroCount());
        statement.bindLong(12, entity.getCreateTime());
        statement.bindLong(13, entity.getUpdateTime());
        if (entity.getExtra1() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getExtra1());
        }
        if (entity.getExtra2() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getExtra2());
        }
      }
    };
    this.__deletionAdapterOfTaskEntity = new EntityDeletionOrUpdateAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tasks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTaskEntity = new EntityDeletionOrUpdateAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tasks` SET `id` = ?,`title` = ?,`description` = ?,`status` = ?,`priority` = ?,`dueDate` = ?,`parentId` = ?,`order` = ?,`aiGenerated` = ?,`syncStatus` = ?,`pomodoroCount` = ?,`createTime` = ?,`updateTime` = ?,`extra1` = ?,`extra2` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindString(4, entity.getStatus());
        statement.bindLong(5, entity.getPriority());
        if (entity.getDueDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDueDate());
        }
        if (entity.getParentId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getParentId());
        }
        statement.bindLong(8, entity.getOrder());
        final int _tmp = entity.getAiGenerated() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindString(10, entity.getSyncStatus());
        statement.bindLong(11, entity.getPomodoroCount());
        statement.bindLong(12, entity.getCreateTime());
        statement.bindLong(13, entity.getUpdateTime());
        if (entity.getExtra1() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getExtra1());
        }
        if (entity.getExtra2() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getExtra2());
        }
        statement.bindLong(16, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteTaskById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tasks WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteChildrenByParentId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tasks WHERE parentId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTaskStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE tasks SET status = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementPomodoroCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE tasks SET pomodoroCount = pomodoroCount + 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTask(final TaskEntity task, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTaskEntity.insertAndReturnId(task);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTasks(final List<TaskEntity> tasks,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTaskEntity.insert(tasks);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTask(final TaskEntity task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTaskEntity.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTask(final TaskEntity task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTaskEntity.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTaskWithChildren(final int taskId,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> TaskDao.DefaultImpls.deleteTaskWithChildren(TaskDao_Impl.this, taskId, __cont), $completion);
  }

  @Override
  public Object deleteTaskById(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTaskById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteTaskById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteChildrenByParentId(final int parentId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteChildrenByParentId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, parentId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteChildrenByParentId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTaskStatus(final int id, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTaskStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateTaskStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementPomodoroCount(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementPomodoroCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfIncrementPomodoroCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TaskEntity>> getAllParentTasks() {
    final String _sql = "SELECT * FROM tasks WHERE parentId IS NULL ORDER BY `order` ASC, createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfAiGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGenerated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoroCount");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfExtra1 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra1");
          final int _cursorIndexOfExtra2 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra2");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getInt(_cursorIndexOfParentId);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final boolean _tmpAiGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAiGenerated);
            _tmpAiGenerated = _tmp != 0;
            final String _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            final String _tmpExtra1;
            if (_cursor.isNull(_cursorIndexOfExtra1)) {
              _tmpExtra1 = null;
            } else {
              _tmpExtra1 = _cursor.getString(_cursorIndexOfExtra1);
            }
            final Integer _tmpExtra2;
            if (_cursor.isNull(_cursorIndexOfExtra2)) {
              _tmpExtra2 = null;
            } else {
              _tmpExtra2 = _cursor.getInt(_cursorIndexOfExtra2);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStatus,_tmpPriority,_tmpDueDate,_tmpParentId,_tmpOrder,_tmpAiGenerated,_tmpSyncStatus,_tmpPomodoroCount,_tmpCreateTime,_tmpUpdateTime,_tmpExtra1,_tmpExtra2);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TaskEntity>> getChildrenByParentId(final int parentId) {
    final String _sql = "SELECT * FROM tasks WHERE parentId = ? ORDER BY `order` ASC, createTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, parentId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfAiGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGenerated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoroCount");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfExtra1 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra1");
          final int _cursorIndexOfExtra2 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra2");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getInt(_cursorIndexOfParentId);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final boolean _tmpAiGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAiGenerated);
            _tmpAiGenerated = _tmp != 0;
            final String _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            final String _tmpExtra1;
            if (_cursor.isNull(_cursorIndexOfExtra1)) {
              _tmpExtra1 = null;
            } else {
              _tmpExtra1 = _cursor.getString(_cursorIndexOfExtra1);
            }
            final Integer _tmpExtra2;
            if (_cursor.isNull(_cursorIndexOfExtra2)) {
              _tmpExtra2 = null;
            } else {
              _tmpExtra2 = _cursor.getInt(_cursorIndexOfExtra2);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStatus,_tmpPriority,_tmpDueDate,_tmpParentId,_tmpOrder,_tmpAiGenerated,_tmpSyncStatus,_tmpPomodoroCount,_tmpCreateTime,_tmpUpdateTime,_tmpExtra1,_tmpExtra2);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTaskById(final int id, final Continuation<? super TaskEntity> $completion) {
    final String _sql = "SELECT * FROM tasks WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TaskEntity>() {
      @Override
      @Nullable
      public TaskEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfAiGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGenerated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoroCount");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfExtra1 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra1");
          final int _cursorIndexOfExtra2 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra2");
          final TaskEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getInt(_cursorIndexOfParentId);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final boolean _tmpAiGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAiGenerated);
            _tmpAiGenerated = _tmp != 0;
            final String _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            final String _tmpExtra1;
            if (_cursor.isNull(_cursorIndexOfExtra1)) {
              _tmpExtra1 = null;
            } else {
              _tmpExtra1 = _cursor.getString(_cursorIndexOfExtra1);
            }
            final Integer _tmpExtra2;
            if (_cursor.isNull(_cursorIndexOfExtra2)) {
              _tmpExtra2 = null;
            } else {
              _tmpExtra2 = _cursor.getInt(_cursorIndexOfExtra2);
            }
            _result = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStatus,_tmpPriority,_tmpDueDate,_tmpParentId,_tmpOrder,_tmpAiGenerated,_tmpSyncStatus,_tmpPomodoroCount,_tmpCreateTime,_tmpUpdateTime,_tmpExtra1,_tmpExtra2);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TaskEntity>> getCompletedTasks() {
    final String _sql = "SELECT * FROM tasks WHERE status = 'COMPLETED' ORDER BY updateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfAiGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGenerated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoroCount");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfExtra1 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra1");
          final int _cursorIndexOfExtra2 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra2");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getInt(_cursorIndexOfParentId);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final boolean _tmpAiGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAiGenerated);
            _tmpAiGenerated = _tmp != 0;
            final String _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            final String _tmpExtra1;
            if (_cursor.isNull(_cursorIndexOfExtra1)) {
              _tmpExtra1 = null;
            } else {
              _tmpExtra1 = _cursor.getString(_cursorIndexOfExtra1);
            }
            final Integer _tmpExtra2;
            if (_cursor.isNull(_cursorIndexOfExtra2)) {
              _tmpExtra2 = null;
            } else {
              _tmpExtra2 = _cursor.getInt(_cursorIndexOfExtra2);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStatus,_tmpPriority,_tmpDueDate,_tmpParentId,_tmpOrder,_tmpAiGenerated,_tmpSyncStatus,_tmpPomodoroCount,_tmpCreateTime,_tmpUpdateTime,_tmpExtra1,_tmpExtra2);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TaskEntity>> getTasksForDate(final long startOfDay, final long endOfDay) {
    final String _sql = "SELECT * FROM tasks WHERE dueDate IS NOT NULL AND dueDate BETWEEN ? AND ? AND parentId IS NULL ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfAiGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGenerated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoroCount");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfExtra1 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra1");
          final int _cursorIndexOfExtra2 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra2");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getInt(_cursorIndexOfParentId);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final boolean _tmpAiGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAiGenerated);
            _tmpAiGenerated = _tmp != 0;
            final String _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            final String _tmpExtra1;
            if (_cursor.isNull(_cursorIndexOfExtra1)) {
              _tmpExtra1 = null;
            } else {
              _tmpExtra1 = _cursor.getString(_cursorIndexOfExtra1);
            }
            final Integer _tmpExtra2;
            if (_cursor.isNull(_cursorIndexOfExtra2)) {
              _tmpExtra2 = null;
            } else {
              _tmpExtra2 = _cursor.getInt(_cursorIndexOfExtra2);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStatus,_tmpPriority,_tmpDueDate,_tmpParentId,_tmpOrder,_tmpAiGenerated,_tmpSyncStatus,_tmpPomodoroCount,_tmpCreateTime,_tmpUpdateTime,_tmpExtra1,_tmpExtra2);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TaskEntity>> searchTasks(final String query) {
    final String _sql = "SELECT * FROM tasks WHERE title LIKE '%' || ? || '%' OR description LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfAiGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGenerated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoroCount");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfExtra1 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra1");
          final int _cursorIndexOfExtra2 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra2");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getInt(_cursorIndexOfParentId);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final boolean _tmpAiGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAiGenerated);
            _tmpAiGenerated = _tmp != 0;
            final String _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            final String _tmpExtra1;
            if (_cursor.isNull(_cursorIndexOfExtra1)) {
              _tmpExtra1 = null;
            } else {
              _tmpExtra1 = _cursor.getString(_cursorIndexOfExtra1);
            }
            final Integer _tmpExtra2;
            if (_cursor.isNull(_cursorIndexOfExtra2)) {
              _tmpExtra2 = null;
            } else {
              _tmpExtra2 = _cursor.getInt(_cursorIndexOfExtra2);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStatus,_tmpPriority,_tmpDueDate,_tmpParentId,_tmpOrder,_tmpAiGenerated,_tmpSyncStatus,_tmpPomodoroCount,_tmpCreateTime,_tmpUpdateTime,_tmpExtra1,_tmpExtra2);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getChildrenList(final int parentId,
      final Continuation<? super List<TaskEntity>> $completion) {
    final String _sql = "SELECT * FROM tasks WHERE parentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, parentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfAiGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGenerated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoroCount");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfExtra1 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra1");
          final int _cursorIndexOfExtra2 = CursorUtil.getColumnIndexOrThrow(_cursor, "extra2");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getInt(_cursorIndexOfParentId);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final boolean _tmpAiGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAiGenerated);
            _tmpAiGenerated = _tmp != 0;
            final String _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            final String _tmpExtra1;
            if (_cursor.isNull(_cursorIndexOfExtra1)) {
              _tmpExtra1 = null;
            } else {
              _tmpExtra1 = _cursor.getString(_cursorIndexOfExtra1);
            }
            final Integer _tmpExtra2;
            if (_cursor.isNull(_cursorIndexOfExtra2)) {
              _tmpExtra2 = null;
            } else {
              _tmpExtra2 = _cursor.getInt(_cursorIndexOfExtra2);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStatus,_tmpPriority,_tmpDueDate,_tmpParentId,_tmpOrder,_tmpAiGenerated,_tmpSyncStatus,_tmpPomodoroCount,_tmpCreateTime,_tmpUpdateTime,_tmpExtra1,_tmpExtra2);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCompletedChildrenCount(final int parentId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM tasks WHERE parentId = ? AND status = 'COMPLETED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, parentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalChildrenCount(final int parentId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM tasks WHERE parentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, parentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
