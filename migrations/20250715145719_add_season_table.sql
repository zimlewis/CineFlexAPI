-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Season] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [Title] NVARCHAR(100) NOT NULL,
    [ReleaseDate] DATE,
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [Description] NVARCHAR(1000),
    [IsDeleted] BIT DEFAULT 0,
    [Show] UNIQUEIDENTIFIER FOREIGN KEY REFERENCES [dbo].[Show]([Id])
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Season];
-- +goose StatementEnd
