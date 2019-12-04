package com.redmadrobot.debug_panel.accounts.data

import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials

class UserCredentialProvider {

    //FIXME Пока данные о тестовых пользователях зашиты в коде. В дальнейшем будут добавляться в рантайме.
   fun getCredentials(): List<DebugUserCredentials> {
      return listOf(
          DebugUserCredentials(
              "7882340482",
              "Qq!11111"
          ),
          DebugUserCredentials(
              "2777248041",
              "Qq!11111"
          ),
          DebugUserCredentials(
              "4183730054",
              "Ww!11111"
          ),
          DebugUserCredentials(
              "1944647499",
              "Qq!11111"
          ),
          DebugUserCredentials(
              "4183730054",
              "Ww!11111"
          ),
          DebugUserCredentials(
              "2387720737",
              "Qq!11111"
          ),
          DebugUserCredentials(
              "7256943541",
              "Qq!11111"
          ),
          DebugUserCredentials(
              "9714981179",
              "Qq!11111"
          )
       )
   }
}
