package com.me.hillfort.models

import com.me.models.SettingsModel

interface SettingsStore {
    fun findAllSettings(): List<SettingsModel>
    fun createSetting(setting: SettingsModel)
    fun updateSetting(setting: SettingsModel)
    fun findOneSetting(id: Long):SettingsModel?
    fun findOneEmail(email: String):SettingsModel?
    fun findOneID(OneSet: SettingsModel?):Long?
    fun findOneName(id: String) : String?
    fun findOnePassword(id: String) : String?
}