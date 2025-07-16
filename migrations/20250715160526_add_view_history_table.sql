-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[ViewHistory] (
    [Account] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Account]([Id]),
    [Episode] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Episode]([Id]),
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [Duration] INT NOT NULL DEFAULT 0,
    [IsDeleted] BIT NOT NULL DEFAULT 0
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[ViewHistory];
-- +goose StatementEnd
