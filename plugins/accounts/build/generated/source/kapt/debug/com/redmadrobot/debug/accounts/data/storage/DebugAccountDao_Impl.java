package com.redmadrobot.debug.accounts.data.storage;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.redmadrobot.debug.accounts.data.model.DebugAccount;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DebugAccountDao_Impl implements DebugAccountDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DebugAccount> __insertionAdapterOfDebugAccount;

  private final EntityDeletionOrUpdateAdapter<DebugAccount> __deletionAdapterOfDebugAccount;

  private final EntityDeletionOrUpdateAdapter<DebugAccount> __updateAdapterOfDebugAccount;

  public DebugAccountDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDebugAccount = new EntityInsertionAdapter<DebugAccount>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `debug_account` (`id`,`login`,`password`,`pin`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DebugAccount value) {
        stmt.bindLong(1, value.getId());
        if (value.getLogin() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getLogin());
        }
        if (value.getPassword() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPassword());
        }
        if (value.getPin() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPin());
        }
      }
    };
    this.__deletionAdapterOfDebugAccount = new EntityDeletionOrUpdateAdapter<DebugAccount>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `debug_account` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DebugAccount value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfDebugAccount = new EntityDeletionOrUpdateAdapter<DebugAccount>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `debug_account` SET `id` = ?,`login` = ?,`password` = ?,`pin` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DebugAccount value) {
        stmt.bindLong(1, value.getId());
        if (value.getLogin() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getLogin());
        }
        if (value.getPassword() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPassword());
        }
        if (value.getPin() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPin());
        }
        stmt.bindLong(5, value.getId());
      }
    };
  }

  @Override
  public Object insert(final DebugAccount account, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDebugAccount.insert(account);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object remove(final DebugAccount account, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfDebugAccount.handle(account);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final DebugAccount account, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfDebugAccount.handle(account);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object getAll(final Continuation<? super List<DebugAccount>> continuation) {
    final String _sql = "SELECT * FROM debug_account";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DebugAccount>>() {
      @Override
      public List<DebugAccount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLogin = CursorUtil.getColumnIndexOrThrow(_cursor, "login");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfPin = CursorUtil.getColumnIndexOrThrow(_cursor, "pin");
          final List<DebugAccount> _result = new ArrayList<DebugAccount>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DebugAccount _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpLogin;
            if (_cursor.isNull(_cursorIndexOfLogin)) {
              _tmpLogin = null;
            } else {
              _tmpLogin = _cursor.getString(_cursorIndexOfLogin);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpPin;
            if (_cursor.isNull(_cursorIndexOfPin)) {
              _tmpPin = null;
            } else {
              _tmpPin = _cursor.getString(_cursorIndexOfPin);
            }
            _item = new DebugAccount(_tmpId,_tmpLogin,_tmpPassword,_tmpPin);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
