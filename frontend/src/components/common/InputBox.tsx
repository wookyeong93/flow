interface InputBoxProps {
  value: string
  placeholder?: string
  disabled?: boolean
  errorMessage?: string
  onChange: (value: string) => void
  onEnter?: () => void
}

export function InputBox({
  value,
  placeholder,
  disabled,
  errorMessage,
  onChange,
  onEnter,
}: InputBoxProps) {
  return (
    <div>
      <input
        type="text"
        value={value}
        placeholder={placeholder}
        disabled={disabled}
        onChange={(e) => onChange(e.target.value)}
        onKeyDown={(e) => {
          if (e.key === 'Enter') onEnter?.()
        }}
        className="border rounded px-3 py-2 w-full disabled:bg-gray-100 disabled:text-gray-400"
      />
      {errorMessage && <p className="text-red-500 text-sm mt-1">{errorMessage}</p>}
    </div>
  )
}
