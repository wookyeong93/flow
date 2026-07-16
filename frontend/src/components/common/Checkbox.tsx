interface CheckboxProps {
  id: string
  label: string
  checked: boolean
  disabled?: boolean
  onChange: (checked: boolean) => void
}

export function Checkbox({ id, label, checked, disabled, onChange }: CheckboxProps) {
  return (
    <label htmlFor={id} className="flex items-center gap-2 cursor-pointer select-none">
      <input
        id={id}
        type="checkbox"
        checked={checked}
        disabled={disabled}
        onChange={(e) => onChange(e.target.checked)}
        className="h-4 w-4 accent-blue-600"
      />
      <span>{label}</span>
    </label>
  )
}
