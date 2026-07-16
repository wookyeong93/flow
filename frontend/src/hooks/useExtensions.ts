import { useEffect, useState } from 'react'
import { ApiError } from '../api/client'
import { extensionsApi } from '../api/extensions'
import { healthApi } from '../api/health'
import type { CustomExtension, FixedExtension } from '../types/extension'

const CONNECTION_ERROR_MESSAGE = '서버에 연결할 수 없습니다. 잠시 후 다시 시도해주세요.'

function toMessage(e: unknown): string {
  if (e instanceof ApiError) return e.message
  return '알 수 없는 오류가 발생했습니다.'
}

export function useExtensions() {
  const [fixedExtensions, setFixedExtensions] = useState<FixedExtension[]>([])
  const [customExtensions, setCustomExtensions] = useState<CustomExtension[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [isConnectionOk , setIsConnectionOk] = useState(false)

  const load = async () => {
    try {
      if(isConnectionOk == false){
        await healthApi.check()
        setIsConnectionOk(true)
      }
      
    } catch {
      setError(CONNECTION_ERROR_MESSAGE)
      setLoading(false)
      return
    }

    try {
      const data = await extensionsApi.getAll()
      setFixedExtensions(data.fixedExtensions)
      setCustomExtensions(data.customExtensions)
    } catch (e) {
      setError(toMessage(e))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
  }, [])

  const toggleFixed = async (id: number, checked: boolean) => {
    setError(null)
    try {
      await extensionsApi.updateFixed(id, { checked })
      await load()
    } catch (e) {
      setError(toMessage(e))
    }
  }

  const addCustom = async (name: string) => {
    setError(null)
    try {
      await extensionsApi.createCustom({ name })
      await load()
      return true
    } catch (e) {
      setError(toMessage(e))
      return false
    }
  }

  const removeCustom = async (id: number) => {
    setError(null)
    try {
      await extensionsApi.deleteCustom(id)
      await load()
    } catch (e) {
      setError(toMessage(e))
    }
  }

  const dismissError = () => setError(null)

  return {
    fixedExtensions,
    customExtensions,
    loading,
    error,
    toggleFixed,
    addCustom,
    removeCustom,
    dismissError,
  }
}
