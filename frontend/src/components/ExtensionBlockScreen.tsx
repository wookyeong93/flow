import { useState } from 'react'
import { useExtensions } from '../hooks/useExtensions'
import { Checkbox } from './common/Checkbox'
import { InputBox } from './common/InputBox'
import { Badge } from './common/Badge'
import { Modal } from './common/Modal'

const NAME_PATTERN = /^[A-Za-z][A-Za-z0-9]*$/
const MAX_CUSTOM_COUNT = 200
const MAX_NAME_LENGTH = 20

function validateName(name: string): string | null {
  if (!name.trim()) return '확장자를 입력해주세요.'
  if (name.length > MAX_NAME_LENGTH) return `최대 ${MAX_NAME_LENGTH}자까지 입력 가능합니다.`
  if (!NAME_PATTERN.test(name)) return '확장자는 영문으로 시작해야 하며 영문/숫자만 입력 가능합니다.'
  return null
}

export function ExtensionBlockScreen() {
  const {
    fixedExtensions,
    customExtensions,
    loading,
    error,
    toggleFixed,
    addCustom,
    removeCustom,
    dismissError,
  } = useExtensions()
  const [inputValue, setInputValue] = useState('')
  const [inputError, setInputError] = useState<string | null>(null)

  const isLimitReached = customExtensions.length >= MAX_CUSTOM_COUNT

  const handleAdd = async () => {
    const validationError = validateName(inputValue)
    if (validationError) {
      setInputError(validationError)
      return
    }
    setInputError(null)
    const success = await addCustom(inputValue.trim())
    if (success) setInputValue('')
  }

  if (loading) return <p>불러오는 중...</p>

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <header>
        <h1 className="text-xl font-bold">파일 확장자 차단</h1>
        <p className="text-gray-500 text-sm">
          파일확장자에 따라 특정 형식의 파일을 첨부하거나 전송하지 못하도록 제한
        </p>
      </header>

      {error && <Modal message={error} onClose={dismissError} />}

      <section className="flex flex-wrap gap-4">
        {fixedExtensions.map((ext) => (
          <Checkbox
            key={ext.id}
            id={`fixed-${ext.id}`}
            label={ext.name}
            checked={ext.checked}
            onChange={(checked) => toggleFixed(ext.id, checked)}
          />
        ))}
      </section>

      <section className="flex gap-2 items-start">
        <InputBox
          value={inputValue}
          placeholder="확장자 입력"
          disabled={isLimitReached}
          errorMessage={inputError ?? undefined}
          onChange={(v) => {
            setInputValue(v)
            setInputError(null)
          }}
          onEnter={handleAdd}
        />
        <button
          type="button"
          onClick={handleAdd}
          disabled={isLimitReached}
          className="shrink-0 rounded bg-blue-600 px-4 py-2 text-white disabled:bg-gray-300 cursor-pointer"
        >
          + 추가
        </button>
      </section>

      <section className="border rounded p-4 space-y-3 min-h-[400px] max-h-[400px] overflow-y-auto">
        <p className="text-sm text-gray-500">
          {customExtensions.length}/{MAX_CUSTOM_COUNT}
        </p>
        <div className="flex flex-wrap gap-2">
          {customExtensions.map((ext) => (
            <Badge key={ext.id} label={ext.name} onRemove={() => removeCustom(ext.id)} />
          ))}
        </div>
      </section>
    </div>
  )
}
