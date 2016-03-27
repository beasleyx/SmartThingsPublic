/**
 *  Nest Direct
 *
 *  Author: dianoga7@3dgo.net
 *  Code: https://github.com/smartthings-users/device-type.nest
 *
 * INSTALLATION
 * =========================================
 * 1) Create a new device type (https://graph.api.smartthings.com/ide/devices)
 *     Name: Nest
 *     Author: dianoga7@3dgo.net
 *     Capabilities:
 *         Polling
 *         Relative Humidity Measurement
 *         Thermostat
 *         Temperature Measurement
 *         Presence Sensor
 *         Sensor
 *     Custom Attributes:
 *         temperatureUnit
 *     Custom Commands:
 *         away
 *         present
 *         setPresence
 *         heatingSetpointUp
 *         heatingSetpointDown
 *         coolingSetpointUp
 *         coolingSetpointDown
 *         setFahrenheit
 *         setCelsius
 *
 * 2) If you want to switch from slider controls to buttons, comment out the slider details line and uncomment the button details line.
 *
 * 3) Create a new device (https://graph.api.smartthings.com/device/list)
 *     Name: Your Choice
 *     Device Network Id: Your Choice
 *     Type: Nest (should be the last option)
 *     Location: Choose the correct location
 *     Hub/Group: Leave blank
 *
 * 4) Update device preferences
 *     Click on the new device to see the details.
 *     Click the edit button next to Preferences
 *     Fill in your information.
 *     To find your serial number, login to http://home.nest.com. Click on the thermostat
 *     you want to control. Under settings, go to Technical Info. Your serial number is
 *     the second item.
 *
 * 5) That's it, you're done.
 *
 * Copyright (C) 2013 Brian Steere <dianoga7@3dgo.net>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

preferences {
	input("username", "text", title: "Username", description: "Your Nest username (usually an email address)")
	input("password", "password", title: "Password", description: "Your Nest password")
	input("serial", "text", title: "Serial #", description: "The serial number of your thermostat")
}

// for the UI
metadata {
	definition (name: "Nest - MultiAttributeTile", namespace: "smartthings-users", author: "dianoga7@3dgo.net") {
		capability "Polling"
		capability "Relative Humidity Measurement"
		capability "Thermostat"
		capability "Temperature Measurement"
		capability "Presence Sensor"
		capability "Sensor"

		command "away"
		command "present"
		command "setPresence"
		command "heatingSetpointUp"
		command "coolingSetpointDown"
		command "setFahrenheit"
		command "setCelsius"
		command "setHumiditySetpoint"
		command "humiditySetpointUp"
		command "humiditySetpointDown"

		attribute "temperatureUnit", "string"
		attribute "humiditySetpoint", "number"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles
    	{
        		       
		tiles(scale: 2) {
		multiAttributeTile(name:"thermostatMulti", type:"thermostat", width:6, height:4, canChangeIcon: true) {
  			
                        
            tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
    		attributeState("default", label:'${currentValue}', unit:"dF")
  			}
  			
	        tileAttribute("device.temperature", key: "VALUE_CONTROL") {
  			attributeState("VALUE_UP",  action:"heatingSetpointUp")
    		attributeState("VALUE_DOWN", action:"coolingSetpointDown")
  			}
  			
            tileAttribute("device.humidity", key: "SECONDARY_CONTROL") {
    		attributeState("default", label:'${currentValue}%', unit:"%")
  			}
  			
            tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
    		attributeState("idle", label: '${name}', backgroundColor:"#616161", icon: "st.thermostat.heating-cooling-off")
    		attributeState("heating", label: '${name}', backgroundColor:"#f44336", icon: "st.thermostat.heat")
    		attributeState("cooling", label: '${name}', backgroundColor:"#2196f3", icon: "st.thermostat.cool")
  			}
            
  			
            tileAttribute("device.thermostatMode", key: "THERMOSTAT_MODE") {
    		attributeState("off", action:"polling.poll", label:'${name}')
    		attributeState("heat", action:"polling.poll", label:'${name}')
    		attributeState("cool", action:"polling.poll", label:'${name}')
    		attributeState("auto", action:"polling.poll", label:'${name}')
  			}
    
		}
        main "thermostatMulti"
  		details "thermostatMulti"
        }
// End of MultiAttribute Tile

      standardTile("thermostatMode", "device.thermostatMode", inactiveLabel: false, decoration: "flat", width:2, height:2) {
			state("auto", action:"polling.poll", icon: "st.thermostat.auto")
			state("off", action:"polling.poll", icon: "st.thermostat.heating-cooling-off")
			state("cool", action:"polling.poll", icon: "st.thermostat.cool")
			state("heat", action:"polling.poll", icon: "st.thermostat.heat")
		}
        
      standardTile("thermostatFanMode", "device.thermostatFanMode", inactiveLabel: true, decoration: "flat", width:2, height:2) {
			state "auto", action:"polling.poll", icon: "st.thermostat.fan-auto"
			state "on", action:"polling.poll", icon: "st.thermostat.fan-on"
			state "circulate", action: "polling.poll", icon: "st.thermostat.fan-circulate"
		}

		valueTile("heatingSetpoint", "device.heatingSetpoint", width:2, height:2) {
			state "default", label:'${currentValue}°', unit:"Heat", backgroundColor:"#f44336", action:"polling.poll"
		}
        
        valueTile("coolingSetpoint", "device.coolingSetpoint", width:2, height:2) {
			state "default", label:'${currentValue}°', unit:"Cool", backgroundColor:"#2196f3", action:"polling.poll"
		}

		standardTile("setPresence", "device.presence", inactiveLabel: false, width:2, height:2) {
			state "present", label: "Home", action:"away", icon: "st.Home.home2", backgroundColor: "#4caf50"
			state "not present", label: "Away", action:"present", icon: "st.Transportation.transportation5", backgroundColor: "#616161"
		}

		standardTile("refresh", "device.thermostatMode", inactiveLabel: true, decoration: "flat", width:2, height:2) {
			state "default", action:"polling.poll", icon:"st.secondary.refresh"
		}
		
        valueTile("airFilter", "device.airFilter", width: 2, height: 2, decoration: "flat", wordWrap: true) {
			state "default", label: '${currentValue}'
		}
		standardTile("leafValue", "device.leafValue", width: 1, height: 1) {
			state("on", icon: "https://dl.dropboxusercontent.com/s/srdilt1iimtihfk/nest_leaf.png")
			state("off", icon: "https://dl.dropboxusercontent.com/s/r8od1fqp7sffrf0/nest_leaf_dark.png")
		}
		valueTile("rssiTile", "device.rssiTile", width: 1, height: 1, decoration: "flat") {
			state "one", label: 'WiFi', icon: "https://dl.dropboxusercontent.com/s/k1hwtntuy7w2nar/signal_wifi_statusbar1.png"
			state "two", label: 'WiFi', icon: "https://dl.dropboxusercontent.com/s/li57n9knv44s6lz/signal_wifi_statusbar2.png"
			state "three", label: 'WiFi', icon: "https://dl.dropboxusercontent.com/s/ceecz74b7jzgeoj/signal_wifi_statusbar3.png"
			state "four", label: 'WiFi', icon: "https://dl.dropboxusercontent.com/s/oa14yhjkelpbx7p/signal_wifi_statusbar4.png"
			state "five", label: 'WiFi', icon: "https://dl.dropboxusercontent.com/s/6kuf1c25niuhxsj/signal_wifi_statusbar5.png"
			state "none", label: 'WiFi', icon: "https://dl.dropboxusercontent.com/s/6oavdtrfxy1b706/signal_wifi_statusbar_null.png"
				
		}

		main(["temperature", "thermostatOperatingState", "humidity"])

        details([	"temperature", "thermostatOperatingState", 
        			"heatingSetpoint", "setPresence", "coolingSetpoint",
                   	"thermostatMode", "airFilter", "refresh"])
                    
/*      details([	"temperature", "thermostatOperatingState", 
        			"heatingSetpointUp", "coolingSetpointUp", "setPresence",
                    "heatingSetpoint", "coolingSetpoint", "thermostatMode", "thermostatFanMode",
                    "heatingSetpointDown", "coolingSetpointDown", "refresh"])            
*/

		// ============================================================

	}

}
 
// update preferences
def updated() {
	log.debug "Updated"
	// reset the authentication
	data.auth = null
}

// parse events into attributes
def parse(String description) {

}

// handle commands

def setFirmwareVer(value) {
	def firmVer = data?.device?.current_version ?: "unknown"
	sendEvent(name: 'firmwareVer', value: firmVer)	
}

def setFilterStatus(hasFilter,filterReminder,filterStatus) {
	def value
	def filterVal = (filterStatus) ? "Ok" : "Replace"
	if (hasFilter) { value = "Installed (${filterVal})" }
	else { value = "Not Installed" }
	//log.info "Air Filter: ${value}"
	sendEvent(name: 'airFilter', value: "Filter:\n" + value)
}

def setLeafStatus(leaf) {
	def val = leaf.toBoolean() ? "on" : "off"
	//log.info "Nest Leaf: ${val}" 
	sendEvent(name: 'leafValue', value: '${val}' )
}

def setHeatingSetpoint(temp) {
	def latestThermostatMode = device.latestState('thermostatMode')
	def temperatureUnit = device.latestValue('temperatureUnit')
  
	switch (temperatureUnit) {
		case "celsius":
			if (temp) {
				if (temp < 9) {
					temp = 9
				}
				if (temp > 32) {
					temp = 32
				}
				if (latestThermostatMode.stringValue == 'auto') {
					api('temperature', ['target_change_pending': true, 'target_temperature_low': temp]) {
						sendEvent(name: 'heatingSetpoint', value: heatingSetpoint, unit: temperatureUnit, state: "heat")
					}
				} else if (latestThermostatMode.stringValue == 'heat') {
					api('temperature', ['target_change_pending': true, 'target_temperature': temp]) {
							sendEvent(name: 'heatingSetpoint', value: heatingSetpoint, unit: temperatureUnit, state: "heat")
					}
				}
			}
			break;
		default:
			if (temp) {
				if (temp < 51) {
					temp = 51
				}
				if (temp > 89) {
					temp = 89
				}
				if (latestThermostatMode.stringValue == 'auto') {
					api('temperature', ['target_change_pending': true, 'target_temperature_low': fToC(temp)]) {
						sendEvent(name: 'heatingSetpoint', value: heatingSetpoint, unit: temperatureUnit, state: "heat")
					}
				} else if (latestThermostatMode.stringValue == 'heat') {
					api('temperature', ['target_change_pending': true, 'target_temperature': fToC(temp)]) {
						sendEvent(name: 'heatingSetpoint', value: heatingSetpoint, unit: temperatureUnit, state: "heat")
					}
				}
			}
			break;
	}
	poll()
}


def coolingSetpointDown(){
	int newSetpoint = device.currentValue("coolingSetpoint") - 1
	log.debug "Setting cool set point down to: ${newSetpoint}"
	setCoolingSetpoint(newSetpoint)
    setHeatingSetpoint(newSetpoint -5)
}

def setCoolingSetpoint(temp) {
	def latestThermostatMode = device.latestState('thermostatMode')
	def temperatureUnit = device.latestValue('temperatureUnit')
	
	switch (temperatureUnit) {
		case "celsius":
			if (temp) {
				if (temp < 9) {
					temp = 9
				}
				if (temp > 32) {
					temp = 32
				}
				if (latestThermostatMode.stringValue == 'auto') {
					api('temperature', ['target_change_pending': true, 'target_temperature_high': temp]) {
						sendEvent(name: 'coolingSetpoint', value: coolingSetpoint, unit: temperatureUnit, state: "cool")
					}
				} else if (latestThermostatMode.stringValue == 'cool') {
					api('temperature', ['target_change_pending': true, 'target_temperature': temp]) {
						sendEvent(name: 'coolingSetpoint', value: coolingSetpoint, unit: temperatureUnit, state: "cool")
					}
				}
			}
			break;
		default:
			if (temp) {
				if (temp < 51) {
					temp = 51
				}
				if (temp > 89) {
					temp = 89
				}
				if (latestThermostatMode.stringValue == 'auto') {
					api('temperature', ['target_change_pending': true, 'target_temperature_high': fToC(temp)]) {
						sendEvent(name: 'coolingSetpoint', value: coolingSetpoint, unit: temperatureUnit, state: "cool")
					}
				} else if (latestThermostatMode.stringValue == 'cool') {
					api('temperature', ['target_change_pending': true, 'target_temperature': fToC(temp)]) {
						sendEvent(name: 'coolingSetpoint', value: coolingSetpoint, unit: temperatureUnit, state: "cool")
					}
				}
			}
			break;
	}
	poll()
}

def heatingSetpointUp(){
	int newSetpoint = device.currentValue("heatingSetpoint") + 1
	log.debug "Setting heat set point up to: ${newSetpoint}"
	setHeatingSetpoint(newSetpoint)
    setCoolingSetpoint(newSetpoint + 5)
}

def setFahrenheit() {
	def temperatureUnit = "fahrenheit"
	log.debug "Setting temperatureUnit to: ${temperatureUnit}"
	sendEvent(name: "temperatureUnit",   value: temperatureUnit)
	poll()
}

def setCelsius() {
	def temperatureUnit = "celsius"
	log.debug "Setting temperatureUnit to: ${temperatureUnit}"
	sendEvent(name: "temperatureUnit",   value: temperatureUnit)
	poll()
}

def humiditySetpointUp(){
	int newSetpoint = device.latestValue("humiditySetpoint") + 1
	log.debug "Setting humidity set point up to: ${newSetpoint}"
	setHumiditySetpoint(newSetpoint)
}

def humiditySetpointDown(){
	int newSetpoint = device.latestValue('humiditySetpoint') - 1
	log.debug "Setting humidity set point down to: ${newSetpoint}"
	setHumiditySetpoint(newSetpoint)
}

def setHumiditySetpoint(humiditySP) {
	if (humiditySP > 0) {
		api('humidity', ['target_humidity': humiditySP]) {
			sendEvent(name: 'humiditySetpoint', value: humiditySetpoint, unit: Humidity)
		}
	}
	poll()
}

def off() {
	setThermostatMode('off')
}

def heat() {
	setThermostatMode('heat')
}

def emergencyHeat() {
	setThermostatMode('heat')
}

def cool() {
	setThermostatMode('cool')
}

def auto() {
	setThermostatMode('range')
}

def setThermostatMode(mode) {
	mode = mode == 'emergency heat'? 'heat' : mode

	api('thermostat_mode', ['target_change_pending': true, 'target_temperature_type': mode]) {
		mode = mode == 'range' ? 'auto' : mode
		sendEvent(name: 'thermostatMode', value: mode)
		poll()
	}
}

def fanOn() {
	setThermostatFanMode('on')
}

def fanAuto() {
	setThermostatFanMode('auto')
}

def fanCirculate() {
	setThermostatFanMode('circulate')
}

def setThermostatFanMode(mode) {
	def modes = [
		on: ['fan_mode': 'on'],
		auto: ['fan_mode': 'auto'],
		circulate: ['fan_mode': 'duty-cycle', 'fan_duty_cycle': 900]
	]

	api('fan_mode', modes.getAt(mode)) {
		sendEvent(name: 'thermostatFanMode', value: mode)
		poll()
	}
}

def away() {
	setPresence('away')
	sendEvent(name: 'presence', value: 'not present')
}

def present() {
	setPresence('present')
	sendEvent(name: 'presence', value: 'present')
}

def setPresence(status) {
	log.debug "Status: $status"
	api('presence', ['away': status == 'away', 'away_timestamp': new Date().getTime(), 'away_setter': 0]) {
 
		poll()
	}
}

def poll() {
	log.debug "Executing 'poll'"
	api('status', []) {
		data.device = it.data.device.getAt(settings.serial)
		data.shared = it.data.shared.getAt(settings.serial)
		data.structureId = it.data.link.getAt(settings.serial).structure.tokenize('.')[1]
		data.structure = it.data.structure.getAt(data.structureId)

		data.device.fan_mode = data.device.fan_mode == 'duty-cycle'? 'circulate' : data.device.fan_mode
		data.structure.away = data.structure.away ? 'away' : 'present'

		log.debug("data.shared: " + data.shared)
		
		def humidity = data.device.current_humidity
		def humiditySetpoint = Math.round(data.device.target_humidity)
		def temperatureType = data.shared.target_temperature_type
		def fanMode = data.device.fan_mode
		def heatingSetpoint = 0
		def coolingSetpoint = 0

		temperatureType = temperatureType == 'range' ? 'auto' : temperatureType

		sendEvent(name: 'humidity', value: humidity)
		sendEvent(name: 'humiditySetpoint', value: humiditySetpoint, unit: Humidity)
		sendEvent(name: 'thermostatFanMode', value: fanMode)
		sendEvent(name: 'thermostatMode', value: temperatureType)

		def temperatureUnit = device.latestValue('temperatureUnit')

		switch (temperatureUnit) {
			case "celsius":
				def temperature = Math.round(data.shared.current_temperature)
				def targetTemperature = Math.round(data.shared.target_temperature)
				
				if (temperatureType == "cool") {
					coolingSetpoint = targetTemperature
				} else if (temperatureType == "heat") {
					heatingSetpoint = targetTemperature
				} else if (temperatureType == "auto") {
					coolingSetpoint = Math.round(data.shared.target_temperature_high)
					heatingSetpoint = Math.round(data.shared.target_temperature_low)
				}

				sendEvent(name: 'temperature', value: temperature, unit: temperatureUnit, state: temperatureType)
				sendEvent(name: 'coolingSetpoint', value: coolingSetpoint, unit: temperatureUnit, state: "cool")
				sendEvent(name: 'heatingSetpoint', value: heatingSetpoint, unit: temperatureUnit, state: "heat")
				break;
			default:
				def temperature = Math.round(cToF(data.shared.current_temperature))
				def targetTemperature = Math.round(cToF(data.shared.target_temperature))				
				
				if (temperatureType == "cool") {
					coolingSetpoint = targetTemperature
				} else if (temperatureType == "heat") {
					heatingSetpoint = targetTemperature
				} else if (temperatureType == "auto") {
					coolingSetpoint = Math.round(cToF(data.shared.target_temperature_high))
					heatingSetpoint = Math.round(cToF(data.shared.target_temperature_low))
				}

				sendEvent(name: 'temperature', value: temperature, unit: temperatureUnit, state: temperatureType)
				sendEvent(name: 'coolingSetpoint', value: coolingSetpoint, unit: temperatureUnit, state: "cool")
				sendEvent(name: 'heatingSetpoint', value: heatingSetpoint, unit: temperatureUnit, state: "heat")
				break;
			}

		switch (device.latestValue('presence')) {
			case "present":
				if (data.structure.away == 'away') {
					sendEvent(name: 'presence', value: 'not present')
				}
				break;
			case "not present":
				if (data.structure.away == 'present') {
					sendEvent(name: 'presence', value: 'present')
				}
				break;
		}

		if (data.shared.hvac_ac_state) {
			sendEvent(name: 'thermostatOperatingState', value: "cooling")
		} else if (data.shared.hvac_heater_state) {
			sendEvent(name: 'thermostatOperatingState', value: "heating")
		} else if (data.shared.hvac_fan_state) {
			sendEvent(name: 'thermostatOperatingState', value: "fan only")
		} else {
			sendEvent(name: 'thermostatOperatingState', value: "idle")
		}
	}
}

def api(method, args = [], success = {}) {
	if(!isLoggedIn()) {
		log.debug "Need to login"
		login(method, args, success)
		return
	}

	def methods = [
		'status': [uri: "/v2/mobile/${data.auth.user}", type: 'get'],
		'fan_mode': [uri: "/v2/put/device.${settings.serial}", type: 'post'],
		'thermostat_mode': [uri: "/v2/put/shared.${settings.serial}", type: 'post'],
		'temperature': [uri: "/v2/put/shared.${settings.serial}", type: 'post'],
		'presence': [uri: "/v2/put/structure.${data.structureId}", type: 'post'],
		'humidity': [uri: "/v2/put/device.${settings.serial}", type: 'post'],
	]

	def request = methods.getAt(method)

	log.debug "Logged in"
	doRequest(request.uri, args, request.type, success)
}

// Need to be logged in before this is called. So don't call this. Call api.
def doRequest(uri, args, type, success) {
	log.debug "Calling $type : $uri : $args"

	if(uri.charAt(0) == '/') {
		uri = "${data.auth.urls.transport_url}${uri}"
	}

	def params = [
		uri: uri,
		headers: [
			'X-nl-protocol-version': 1,
			'X-nl-user-id': data.auth.userid,
			'Authorization': "Basic ${data.auth.access_token}"
		],
		body: args
	]

	def postRequest = { response ->
		if (response.getStatus() == 302) {
			def locations = response.getHeaders("Location")
			def location = locations[0].getValue()
			log.debug "redirecting to ${location}"
			doRequest(location, args, type, success)
		} else {
			success.call(response)
		}
	}

	try {
		if (type == 'post') {
			httpPostJson(params, postRequest)
		} else if (type == 'get') {
			httpGet(params, postRequest)
		}
	} catch (Throwable e) {
		login()
	}
}

def login(method = null, args = [], success = {}) {
	def params = [
		uri: 'https://home.nest.com/user/login',
		body: [username: settings.username, password: settings.password]
	]

	httpPost(params) {response ->
		data.auth = response.data
		data.auth.expires_in = Date.parse('EEE, dd-MMM-yyyy HH:mm:ss z', response.data.expires_in).getTime()
		log.debug data.auth

		api(method, args, success)
	}
}

def isLoggedIn() {
	if(!data.auth) {
		log.debug "No data.auth"
		return false
	}

	def now = new Date().getTime();
	return data.auth.expires_in > now
}

def cToF(temp) {
	return (temp * 1.8 + 32).toDouble()
}

def fToC(temp) {
	return ((temp - 32) / 1.8).toDouble()
}