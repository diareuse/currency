package wiki.depasquale.currency.platform

import android.app.backup.BackupAgent
import android.app.backup.BackupDataInput
import android.app.backup.BackupDataOutput
import android.os.ParcelFileDescriptor

class BackupAgentNoop : BackupAgent() {

    override fun onBackup(
        oldState: ParcelFileDescriptor?,
        output: BackupDataOutput?,
        newState: ParcelFileDescriptor?
    ) {
        /* no-op */
    }

    override fun onRestore(
        data: BackupDataInput?,
        versionCode: Int,
        newState: ParcelFileDescriptor?
    ) {
        /* no-op */
    }

}